package com.bangkit.nanaspos.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.nanaspos.UserPreference
import com.bangkit.nanaspos.api.ApiConfig
import com.bangkit.nanaspos.api.LoginResponse
import com.bangkit.nanaspos.ui.home.HomeActivity
import com.bangkit.nanaspos.api.LoginRequest
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel: ViewModel() {

    var isLoading by mutableStateOf(false)
    var username by mutableStateOf("")
    var password by mutableStateOf("")

    fun login(context: Context){
        viewModelScope.launch {
            val service  = ApiConfig.getApiService()
            isLoading = true
            try {
                val request = LoginRequest(
                    username = username,
                    password = password
                )

                val response = service.login(request)
                if (response.isSuccessful){
                    val result = response.body()!!
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()

                    if (!result.error){
                        //save user pref
                        val pref = UserPreference(context)
                        pref.setUser(result.data)

                        //to another activity
                        val activity = context as Activity
                        activity.startActivity(Intent(activity, HomeActivity::class.java))
                    }
                }
            } catch (e: Exception){

            }

            isLoading = false
        }
    }
}
