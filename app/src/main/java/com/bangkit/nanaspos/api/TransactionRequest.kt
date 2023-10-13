package com.bangkit.nanaspos.api

import com.google.gson.annotations.SerializedName

data class TransactionRequest(

	@field:SerializedName("grandTotal")
	val grandTotal: Int? = null,

	@field:SerializedName("diskon")
	val diskon: Int? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("divisi")
	val divisi: Int? = null,

	@field:SerializedName("customer")
	val customer: String? = null,

	@field:SerializedName("items")
	val items: List<TransactionItem?>? = null
)

data class TransactionItem(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("harga")
	val harga: Int? = null,

	@field:SerializedName("subtotal")
	val subtotal: Int? = null,

	@field:SerializedName("qty")
	val qty: Int? = null
)
