package com.bangkit.nanaspos.ui.checkout

import android.app.Activity
import android.content.Context
import android.content.Intent
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
    var grandTotal by mutableStateOf(0)
    var diskon by mutableStateOf(0)
    var finalTotal by mutableStateOf(0)
    var diskonTotal by mutableStateOf(0)
    var isLoading by mutableStateOf(false)
    var alamat by mutableStateOf("")
    var ongkir by mutableStateOf(0)

    fun getCheckoutList(){
        val mainList = MainViewModel.checkOutList
        menuList = mainList
        countSubtotal()
        countDiscount()
    }

    private fun countSubtotal(){
        var subtotal = 0
        menuList.value.forEachIndexed { index, menu ->
            subtotal += menu.subTotal
        }
        this.grandTotal = subtotal
    }

    fun countDiscount(){
        diskonTotal = (grandTotal / 100) * (diskon)
        finalTotal = (grandTotal / 100) * (100-diskon)
    }

    fun createTransaction(context: Context, customer: String){
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
            grandTotal = finalTotal,
            customer = customer,
            divisi = 1,
            userId = 1,
            diskon = grandTotal - finalTotal ,
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