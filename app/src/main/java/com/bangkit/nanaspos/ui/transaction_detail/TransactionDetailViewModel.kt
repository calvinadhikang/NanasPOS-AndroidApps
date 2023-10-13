package com.bangkit.nanaspos.ui.transaction_detail

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.bangkit.nanaspos.api.ApiConfig
import com.bangkit.nanaspos.api.FinishTransactionResponse
import com.bangkit.nanaspos.api.GetTransactionDetailResponse
import com.bangkit.nanaspos.api.MenuItemResponse
import com.bangkit.nanaspos.api.TransactionItemResponse
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TransactionDetailViewModel: ViewModel() {

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
            dtrans = emptyList()
        )
    )

    var kembalian by mutableStateOf(0)
    var uang by mutableStateOf(0)
    var responseMessage by mutableStateOf("Loading Transaksi...")

    fun countKembalian(){
        kembalian = uang - htrans.grandtotal
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

    fun printBluetooth(){
        var details = ""
        detailList.value.forEachIndexed { index, menuItemResponse ->
            details += "[L]<b>${menuItemResponse.nama}</b>[R]Rp. ${String.format("%,d", menuItemResponse.subtotal)}\n"
            details += "[L]${menuItemResponse.qty} x Rp. ${String.format("%,d", menuItemResponse.harga)}\n"
            details += "[L]\n"
        }

        var discount = ""
        if (htrans.diskon > 0){
            discount = "[R]TOTAL DISC  :[R]Rp. ${String.format("%,d", htrans.diskon)}\n" + "[R]GRAND TOTAL :[R]Rp. ${String.format("%,d", htrans.grandtotal)}\n"
        }

        val printer = EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32)
        printer
            .printFormattedText(
            "[C]<u><font size='big'>Babiku Genyol</font></u>\n" +
                "[C]WA: 081-217-393-280\n" +
                "[L]<b>Customer :${htrans.customer}</b>\n" +
                "[L]\n" +
                "[C]================================\n" +
                "[L]\n" +
                "${details}\n" +
                "[C]--------------------------------\n" +
                "[R]TOTAL PRICE :[R]Rp. ${String.format("%,d", htrans.grandtotal + htrans.diskon)}\n" +
                "${discount}}"
            )
    }
}