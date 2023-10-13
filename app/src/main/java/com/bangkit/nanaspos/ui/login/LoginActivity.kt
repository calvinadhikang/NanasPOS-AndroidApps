package com.bangkit.nanaspos.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bangkit.nanaspos.UserPreference
import com.bangkit.nanaspos.ui.component.LoadingComponent
import com.bangkit.nanaspos.ui.home.HomeActivity
import com.bangkit.nanaspos.ui.theme.NanasPOSTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NanasPOSTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit){
        val pref = UserPreference(context)
        val user = pref.getUser()
        Log.e("USER_PREF", user.nama)
        if (user.id != -1){
            //to another activity
            val activity = context as Activity
            activity.startActivity(Intent(activity, HomeActivity::class.java))
        }
    }

    var username by rememberSaveable{ mutableStateOf("") }
    var password by rememberSaveable{ mutableStateOf("") }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier.padding(16.dp)
        ) {
            Text(
                text = "Nanas Pos App",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 40.sp,
                modifier = modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = modifier.height(12.dp))
            Text(text = "Username", fontWeight = FontWeight.Medium, fontSize = 20.sp)
            OutlinedTextField(value = username, onValueChange = { username = it }, modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp))
            Text(text = "Password", fontWeight = FontWeight.Medium, fontSize = 20.sp)
            OutlinedTextField(value = password, onValueChange = { password = it }, modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp))
            if (viewModel.isLoading){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = modifier.fillMaxWidth()
                ) {
                    LoadingComponent()
                }
            }else{
                Button(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(60.dp)
                    ,
                    onClick = {
                        viewModel.login(username, password, context)
                    }) {
                    Text(text = "Login", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    NanasPOSTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LoginScreen()
        }
    }
}