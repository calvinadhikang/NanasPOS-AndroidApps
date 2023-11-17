package com.bangkit.nanaspos.ui.checkout

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.bangkit.nanaspos.api.ApiConfig
import com.bangkit.nanaspos.api.TransactionItem
import com.bangkit.nanaspos.api.TransactionRequest
import com.bangkit.nanaspos.api.TransactionResponse
import com.bangkit.nanaspos.model.Menu
import com.bangkit.nanaspos.ui.home.HomeActivity
import com.bangkit.nanaspos.ui.main.MainViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutViewModel: ViewModel() {

    var menuList: MutableStateFlow<List<Menu>> = MutableStateFlow(mutableStateListOf())
    var total = MutableStateFlow(0)
    var pajak_value = MutableStateFlow(0)
    var diskon by mutableStateOf(0)

    var finalTotal by mutableStateOf(0)
    var diskonTotal by mutableStateOf(0)
    var customer by mutableStateOf("")
    var isLoading by mutableStateOf(false)

    var pajak = 10

    fun getCheckoutList(){
        val mainList = MainViewModel.checkOutList
        menuList.value = mainList.value

        countTotal()
        countPPN()
    }

    private fun countPPN() {
        pajak_value.value = total.value / 100 * pajak
    }

    private fun countTotal() {
        var temp = 0
        menuList.value.map { menu -> temp += menu.subTotal }
        total.value = temp
    }

    fun countDiscount(){
        diskonTotal = (total.value / 100) * (diskon)
        finalTotal = (total.value / 100) * (100-diskon)
    }

    fun createTransaction(context: Context){
        isLoading = true

        val itemsList = mutableListOf<TransactionItem>()
        menuList.value.forEachIndexed { index, menu ->
            itemsList.add(
                TransactionItem(
                    qty = menu.qty,
                    nama = menu.nama,
                    harga = menu.harga,
                    subtotal = menu.subTotal
            ))
        }

        val request = TransactionRequest(
            customer = customer,
            divisi = 1,
            userId = 1,
            diskon = diskonTotal ,
            items = itemsList
        )

        val client = ApiConfig.getApiService().createTransaction(request)
        client.enqueue(object: Callback<TransactionResponse> {
            override fun onResponse(
                call: Call<TransactionResponse>,
                response: Response<TransactionResponse>
            ) {
                if (response.isSuccessful){
                    if (!response.body()!!.error){
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_LONG).show()
                        isLoading = false

                        val activity = context as Activity

                        //reset
                        activity.finish()
                        MainViewModel.checkOutList = MutableStateFlow(mutableStateListOf())

                        val intent = Intent(activity, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                        activity.startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
            }
        })
    }
}