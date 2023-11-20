package com.bangkit.nanaspos.repository

import com.bangkit.nanaspos.api.ApiService
import com.bangkit.nanaspos.api.MenuResponse
import retrofit2.Response

class MenuRepository(
    private val api: ApiService
) {
    suspend fun getAllMenus(divisi: Int): Response<MenuResponse> {
        return api.fetchMenus(divisi)
    }
}