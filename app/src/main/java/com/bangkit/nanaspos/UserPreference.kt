package com.bangkit.nanaspos

import android.content.Context
import com.bangkit.nanaspos.api.UserDetailResponse

internal class UserPreference(context: Context) {

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(value: UserDetailResponse) {
        val editor = preferences.edit()
        editor.putInt(ID, value.id)
        editor.putInt(DIVISI, value.divisi)
        editor.putString(NAMA, value.nama)
        editor.apply()
    }

    fun getUser(): UserDetailResponse {
        val id = preferences.getInt(ID, -1)
        val divisi = preferences.getInt(DIVISI, -1)
        val nama = preferences.getString(NAMA, "")

        return UserDetailResponse(id, divisi, nama!!)
    }

    fun clearUser(){
        preferences.edit().clear().commit()
    }

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val NAMA = "nama"
        private const val ID = "id"
        private const val DIVISI = "divisi"
    }
}