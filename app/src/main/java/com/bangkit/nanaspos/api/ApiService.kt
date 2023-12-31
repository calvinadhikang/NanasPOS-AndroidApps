package com.bangkit.nanaspos.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("api/transaksi/delete/{id}")
    fun deleteTransaction(
        @Path("id") id:Int
    ): Call<FinishTransactionResponse>

    @GET("api/menu/{divisi}")
    suspend fun fetchMenus(
        @Path("divisi") divisi:Int
    ): Response<MenuResponse>

    @GET("api/transaksi/{divisi}")
    fun fetchTransactions(
        @Path("divisi") divisi:Int
    ): Call<GetTransactionResponse>

    @GET("api/transaksi/detail/{id}")
    fun fetchTransactionsDetail(
        @Path("id") id:Int
    ): Call<GetTransactionDetailResponse>

    @POST("api/transaksi/finish/{id}")
    fun finishTransactions(
        @Path("id") id:Int
    ): Call<FinishTransactionResponse>

    @POST("api/transaksi")
    suspend fun createTransaction(
        @Body request: TransactionRequest
    ): Response<TransactionResponse>

    @POST("api/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
}