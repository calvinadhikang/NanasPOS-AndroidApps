package com.bangkit.nanaspos.ui.main

import android.util.Log
import com.bangkit.nanaspos.model.Menu
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.nanaspos.api.ApiConfig
import com.bangkit.nanaspos.api.MenuResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {
    var menuList: MutableStateFlow<List<Menu>> = MutableStateFlow(mutableStateListOf())

    var responseMessage by mutableStateOf("Loading Menu...")
    var loading by mutableStateOf(true)
    var subTotal by mutableStateOf(0)

    fun getMenu(divisi: Int){
        viewModelScope.launch{
            responseMessage = "Loading Menu..."
            loading = true
            val service = ApiConfig.getApiService()
            val response = service.fetchMenus(divisi)
            if (response.isSuccessful){
                val result = response.body()!!

                responseMessage = result.message
                loading = false

                val list = mutableStateListOf<Menu>()
                result.data.forEachIndexed { index, menuItemResponse ->
                    list.add(Menu(
                        id = menuItemResponse.id!!,
                        qty = menuItemResponse.qty!!,
                        subTotal = menuItemResponse.subtotal!!,
                        harga = menuItemResponse.harga!!,
                        nama = menuItemResponse.nama!!
                    ))
                }

                menuList.value = list
                countSubtotal()
            }
        }
    }

    fun modifyQty(itemId: Int, oldQty: Int, oldHarga: Int, modifyValue: Int){
        val updatedItems = menuList.value.toMutableList()
        val newQty = oldQty + modifyValue
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

    fun checkout(){
        val list = mutableStateListOf<Menu>()
        menuList.value.forEachIndexed { index, menu ->
            if (menu.qty > 0){
                list.add(menu)
            }
        }
        checkOutList = MutableStateFlow(list)
    }

    companion object{
        var checkOutList: MutableStateFlow<List<Menu>> = MutableStateFlow(mutableStateListOf())
    }
}