package com.bangkit.nanaspos.api

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("password")
    val password: String? = null,
)
