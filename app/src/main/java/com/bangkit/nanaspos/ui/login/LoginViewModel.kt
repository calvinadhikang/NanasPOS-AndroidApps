package com.bangkit.nanaspos.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.bangkit.nanaspos.UserPreference
import com.bangkit.nanaspos.api.ApiConfig
import com.bangkit.nanaspos.api.LoginResponse
import com.bangkit.nanaspos.ui.home.HomeActivity
import com.bangkit.nanaspos.api.LoginRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel: ViewModel() {

    var isLoading by mutableStateOf(false)

    fun login(user: String, pass: String, context: Context){
        isLoading = true

        val request = LoginRequest(
            username = user,
            password = pass
        )
        val client = ApiConfig.getApiService().login(request)
        client.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                isLoading = false
                if (response.isSuccessful){
                    val body = response.body()!!

                    Toast.makeText(context, body.message, Toast.LENGTH_SHORT).show()
                    if (!body.error){
                        //save user pref
                        val pref = UserPreference(context)
                        pref.setUser(body.data)

                        //to another activity
                        val activity = context as Activity
                        activity.startActivity(Intent(activity, HomeActivity::class.java))
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            }
        })
    }
}
