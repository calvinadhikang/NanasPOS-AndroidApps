package com.bangkit.nanaspos.api

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("api/transaksi/delete/{id}")
    fun deleteTransaction(
        @Path("id") id:Int
    ): Call<FinishTransactionResponse>

    @GET("api/menu/{divisi}")
    fun fetchMenus(
        @Path("divisi") divisi:Int
    ): Call<MenuResponse>

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
    fun createTransaction(
        @Body request: TransactionRequest
    ): Call<TransactionResponse>

    @POST("api/login")
    fun login(
        @Body request: LoginRequest
    ): Call<LoginResponse>
}