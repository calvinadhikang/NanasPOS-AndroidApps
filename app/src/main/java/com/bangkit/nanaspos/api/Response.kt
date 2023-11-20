package com.bangkit.nanaspos.api

import com.google.gson.annotations.SerializedName

data class FinishTransactionResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("data")
    val data: String
)

data class GetTransactionDetailResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("data")
    val data: TransactionItemResponse
)

data class GetTransactionResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("data")
    val data: List<TransactionItemResponse>
)

data class TransactionItemResponse(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("user_id")
    val user_id: Int,

    @field:SerializedName("divisi")
    val divisi: Int,

    @field:SerializedName("customer")
    val customer: String,

    @field:SerializedName("grandtotal")
    val grandtotal: Int,

    @field:SerializedName("tax")
    val tax: Int,

    @field:SerializedName("tax_value")
    val tax_value: Int,

    @field:SerializedName("total")
    val total: Int,

    @field:SerializedName("diskon")
    val diskon: Int,

    @field:SerializedName("status")
    val status: Int,

    @field:SerializedName("created_at")
    val createdAt: String?,

    @field:SerializedName("dtrans")
    val dtrans: List<MenuItemResponse?>?,
)

data class LoginResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("data")
    val data: UserDetailResponse
)

data class UserDetailResponse(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("divisi")
    val divisi: Int,

    @field:SerializedName("nama")
    val nama: String
)

data class TransactionResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("data")
    val data: List<MenuItemResponse>
)

data class MenuResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("data")
    val data: List<MenuItemResponse>
)

data class MenuItemResponse(

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("harga")
    val harga: Int? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("kategori")
    val kategori: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("qty")
    var qty: Int? = null,

    @field:SerializedName("subtotal")
    var subtotal: Int? = null,

    @field:SerializedName("divisi")
    val divisi: Int? = null
)