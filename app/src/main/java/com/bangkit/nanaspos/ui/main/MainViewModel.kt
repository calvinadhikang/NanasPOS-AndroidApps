package com.bangkit.nanaspos.ui.main

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.bangkit.nanaspos.model.Menu
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
import com.bangkit.nanaspos.api.MenuResponse
import com.bangkit.nanaspos.repository.MenuRepository
import com.bangkit.nanaspos.ui.checkout.CheckoutActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(
    private val service: ApiService,
    val context: Context
) : ViewModel() {
    var menuList: MutableStateFlow<List<Menu>> = MutableStateFlow(mutableStateListOf())
    private val pref = UserPreference(context).getUser()

    var loading by mutableStateOf(true)
    var subTotal by mutableIntStateOf(0)

    fun getMenu(){
        viewModelScope.launch{
            loading = true

            val menuRepository = MenuRepository(service)
            val response = menuRepository.getAllMenus(pref.divisi)
            if (response.isSuccessful){
                val result = response.body()!!

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
            loading = false
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

    fun checkout(){
        if (subTotal <= 0){
            Toast.makeText(context, "Pilih Menu Minimal 1", Toast.LENGTH_LONG).show()
            return
        }

        val list = mutableStateListOf<Menu>()
        menuList.value.forEachIndexed { index, menu ->
            if (menu.qty > 0){
                list.add(menu)
            }
        }
        checkOutList = MutableStateFlow(list)

        //pindah Halaman
        context.startActivity(Intent(context, CheckoutActivity::class.java))
    }

    companion object{
        var checkOutList: MutableStateFlow<List<Menu>> = MutableStateFlow(mutableStateListOf())
    }
}