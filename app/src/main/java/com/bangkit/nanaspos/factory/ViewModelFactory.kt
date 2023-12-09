package com.bangkit.nanaspos.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.nanaspos.api.ApiService
import com.bangkit.nanaspos.ui.main.MainViewModel
import com.bangkit.nanaspos.ui.transaction_detail.TransactionDetailViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val api: ApiService,
    private val context: Context,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(api, context) as T
        }
        if (modelClass.isAssignableFrom(TransactionDetailViewModel::class.java)){
            return TransactionDetailViewModel(api, context) as T
        }
        return super.create(modelClass)
    }
}