package com.bangkit.nanaspos.ui.transaction_detail

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.nanaspos.UserPreference
import com.bangkit.nanaspos.api.ApiConfig
import com.bangkit.nanaspos.api.ApiService
import com.bangkit.nanaspos.api.FinishTransactionResponse
import com.bangkit.nanaspos.api.GetTransactionDetailResponse
import com.bangkit.nanaspos.api.MenuItemResponse
import com.bangkit.nanaspos.api.TransactionItemResponse
import com.bangkit.nanaspos.model.Menu
import com.bangkit.nanaspos.repository.MenuRepository
import com.bangkit.nanaspos.util.getDateTime
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TransactionDetailViewModel(
    val service: ApiService,
    val context: Context,
): ViewModel() {
    var isEdit = MutableStateFlow(false)
    var printAble by mutableStateOf(false)
    var loading by mutableStateOf(false)

    var detailList: MutableStateFlow<List<MenuItemResponse>> = MutableStateFlow(
        mutableStateListOf()
    )
    var htrans by mutableStateOf(
        TransactionItemResponse(
            id = -1,
            divisi = -1,
            customer = "",
            grandtotal = -1,
            user_id = -1,
            status = -1,
            diskon = -1,
            dtrans = emptyList(),
            tax = -1,
            tax_value = -1,
            total = -1,
            createdAt = ""
        )
    )

    var kembalian by mutableStateOf(0)
    var uang by mutableStateOf(0)
    var responseMessage by mutableStateOf("Loading Transaksi...")

    fun countKembalian(){
        kembalian = uang - htrans.grandtotal
    }

    fun toggleEdit(){
        isEdit.value = !isEdit.value
    }

    fun getTransactionDetail(id: Int){
        loading = true
        val client = ApiConfig.getApiService().fetchTransactionsDetail(id)
        client.enqueue(object: Callback<GetTransactionDetailResponse> {
            override fun onResponse(
                call: Call<GetTransactionDetailResponse>,
                response: Response<GetTransactionDetailResponse>
            ) {
                if (response.isSuccessful){
                    val body = response.body()!!
                    printAble = true
                    loading = false
                    responseMessage = body.message

                    //set htrans
                    htrans = body.data
                    uang = htrans.grandtotal
                    //populate detail trans
                    val list = mutableStateListOf<MenuItemResponse>()
                    body.data.dtrans!!.forEachIndexed { index, menuItemResponse ->
                        list.add(menuItemResponse!!)
                    }
                    detailList = MutableStateFlow(list)
                }
            }

            override fun onFailure(call: Call<GetTransactionDetailResponse>, t: Throwable) {
            }
        })
    }

    fun finishTransaction(id: Int, context: Context){
        val client = ApiConfig.getApiService().finishTransactions(id)
        client.enqueue(object: Callback<FinishTransactionResponse> {
            override fun onResponse(
                call: Call<FinishTransactionResponse>,
                response: Response<FinishTransactionResponse>
            ) {
                Log.e("TEST", "TEST")
                if (!response.body()!!.error){
                    getTransactionDetail(id)
                    Toast.makeText(context, "Transaksi Lunas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FinishTransactionResponse>, t: Throwable) {
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun printBluetooth(context: Context){
        viewModelScope.launch {
            var nama = if (htrans.divisi == 1) { "BabikuGenyol" } else { "BaliLais" }

            var details = ""
            detailList.value.forEachIndexed { index, menuItemResponse ->
                details += "[L]<b>${menuItemResponse.nama}</b>\n"
                details += "[L]${menuItemResponse.qty} x Rp.${String.format("%,d", menuItemResponse.harga)} [R]Rp.${String.format("%,d", menuItemResponse.subtotal)}\n"
            }

            var discount = ""
            if (htrans.diskon > 0){
                discount = "[R]TOTAL DISC  :[R]Rp.${String.format("%,d", htrans.diskon)}\n" + "[R]GRAND TOTAL :[R]Rp.${String.format("%,d", htrans.grandtotal)}\n"
            }

            var tax = ""
            if (htrans.tax > 0){
                tax = "[L]Tax ${htrans.tax}% : [R]Rp.${String.format("%,d", htrans.tax_value)}\n"
            }

            val printer = EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32)
            printer
                .printFormattedText(
                "[C]<u><font size='big'>$nama</font></u>\n" +
                    "[C]WA: 081-217-393-280\n" +
                    "[L]<b>Customer :${htrans.customer}</b>\n" +
                    "[L]Tanggal [R]${getDateTime(htrans.createdAt!!)}\n" +
                    "[C]================================\n" +
                    "${details}\n" +
                    "[C]--------------------------------\n" +
                    "[L]TOTAL HARGA :[R]Rp.${String.format("%,d", htrans.total)}\n" +
                    "${tax}" +
                    "[L]GRAND TOTAL :[R]Rp.${String.format("%,d", htrans.grandtotal)}"
                )
        }
    }


    //
    var menuList: MutableStateFlow<List<Menu>> = MutableStateFlow(mutableStateListOf())
    private val pref = UserPreference(context).getUser()

    var loadingMenu by mutableStateOf(true)
    var subTotal by mutableIntStateOf(0)

    fun getMenu(){
        viewModelScope.launch{
            loadingMenu = true

            val menuRepository = MenuRepository(service)
            val response = menuRepository.getAllMenus(pref.divisi)
            if (response.isSuccessful){
                val result = response.body()!!

                val list = mutableStateListOf<Menu>()
                result.data.forEachIndexed { index, menuItemResponse ->
                    list.add(
                        Menu(
                        id = menuItemResponse.id!!,
                        qty = menuItemResponse.qty!!,
                        subTotal = menuItemResponse.subtotal!!,
                        harga = menuItemResponse.harga!!,
                        nama = menuItemResponse.nama!!
                    )
                    )
                }

                menuList.value = list
                countSubtotal()
            }
            loadingMenu = false
        }
    }

    fun modifyQty(itemId: Int, oldQty: Int, oldHarga: Int, modifyValue: Int){
        val updatedItems = menuList.value.toMutableList()
        val newQty = oldQty + modifyValue

        //checkQty
        if (newQty < 0){
            Toast.makeText(context, "Qty minimal 0", Toast.LENGTH_SHORT).show()
            return
        }

        val newSubtotal = newQty * oldHarga
        val itemIndex = updatedItems.indexOfFirst { it.id == itemId }
        if (itemIndex != -1) {
            val updatedItem = updatedItems[itemIndex].copy(qty = newQty, subTotal = newSubtotal)
            updatedItems[itemIndex] = updatedItem
            menuList.value = updatedItems
        }

        countSubtotal()
    }

    private fun countSubtotal(){
        var subtotal = 0
        menuList.value.forEachIndexed { index, menu ->
            subtotal += menu.subTotal
        }
        this.subTotal = subtotal
    }
}
