package com.bangkit.nanaspos.ui.transaction

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.bangkit.nanaspos.api.ApiConfig
import com.bangkit.nanaspos.api.FinishTransactionResponse
import com.bangkit.nanaspos.api.GetTransactionResponse
import com.bangkit.nanaspos.api.TransactionItemResponse
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransactionViewModel: ViewModel() {
    var transList: MutableStateFlow<List<TransactionItemResponse>> = MutableStateFlow(mutableStateListOf())

    var responseMessage by mutableStateOf("Loading Transaksi...")
    var grandTotal by mutableStateOf(0)
    var loading by mutableStateOf(true)

    fun getTransaction(divisi: Int){
        responseMessage = "Loading Transaksi..."
        loading = true
        grandTotal = 0

        val client = ApiConfig.getApiService().fetchTransactions(divisi)
        client.enqueue(object: Callback<GetTransactionResponse> {
            override fun onResponse(
                call: Call<GetTransactionResponse>,
                response: Response<GetTransactionResponse>
            ) {
                if (response.isSuccessful){
                    val body = response.body()!!
                    responseMessage = body.message
                    loading = false

                    val list = mutableStateListOf<TransactionItemResponse>()
                    body.data.forEachIndexed { index, transactionItemResponse ->
                        list.add(transactionItemResponse)
                        //count total pendapatan
                        grandTotal += transactionItemResponse.grandtotal
                    }
                    transList = MutableStateFlow(list)
                }
            }

            override fun onFailure(call: Call<GetTransactionResponse>, t: Throwable) {
            }
        })
    }

    fun deleteTransaction(id: Int, divisi: Int){
        val client = ApiConfig.getApiService().deleteTransaction(id)
        client.enqueue(object: Callback<FinishTransactionResponse> {
            override fun onResponse(
                call: Call<FinishTransactionResponse>,
                response: Response<FinishTransactionResponse>
            ) {
                getTransaction(divisi)
            }

            override fun onFailure(call: Call<FinishTransactionResponse>, t: Throwable) {
            }
        })
    }
}

