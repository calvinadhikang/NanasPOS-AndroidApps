package com.bangkit.nanaspos.ui.main

import android.util.Log
import com.bangkit.nanaspos.model.Menu
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.bangkit.nanaspos.api.ApiConfig
import com.bangkit.nanaspos.api.MenuResponse
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {
    var menuList: MutableStateFlow<List<Menu>> = MutableStateFlow(mutableStateListOf())

    var responseMessage by mutableStateOf("Loading Menu...")
    var loading by mutableStateOf(true)
    var subTotal by mutableStateOf(0)

    fun getMenu(divisi: Int){
        responseMessage = "Loading Menu..."
        loading = true

        val client = ApiConfig.getApiService().fetchMenus(divisi)
        client.enqueue(object: Callback<MenuResponse> {
            override fun onResponse(call: Call<MenuResponse>, response: Response<MenuResponse>) {
                if (response.isSuccessful){
                    val body = response.body()!!

                    responseMessage = body.message
                    loading = false

                    val list = mutableStateListOf<Menu>()
                    body.data.forEachIndexed { index, menuItemResponse ->
                        list.add(Menu(
                            id = menuItemResponse.id!!,
                            qty = menuItemResponse.qty!!,
                            subTotal = menuItemResponse.subtotal!!,
                            harga = menuItemResponse.harga!!,
                            nama = menuItemResponse.nama!!
                        ))
                    }
                    Log.e("MENU_REFRESH", "MENU_REFRESH")
                    menuList.value = list
                    countSubtotal()
                }
            }

            override fun onFailure(call: Call<MenuResponse>, t: Throwable) {
            }

        })
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