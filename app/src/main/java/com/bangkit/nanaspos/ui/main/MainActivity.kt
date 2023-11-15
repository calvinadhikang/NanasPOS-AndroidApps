package com.bangkit.nanaspos.ui.main

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bangkit.nanaspos.UserPreference
import com.bangkit.nanaspos.ui.checkout.CheckoutActivity
import com.bangkit.nanaspos.ui.component.LoadingComponent
import com.bangkit.nanaspos.ui.component.MenuListComponent
import com.bangkit.nanaspos.ui.theme.NanasPOSTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddScreen(
    viewModel: MainViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val pref = UserPreference(context).getUser()
    val menuList by viewModel.menuList.collectAsState()

    LaunchedEffect(key1 = Unit){
        viewModel.getMenu(pref.divisi)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            text = "List Menu",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )
        Button(
            onClick = { viewModel.getMenu(pref.divisi) }
        ) {
            Text("Reset Menu")
        }

        if (viewModel.loading){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LoadingComponent()
            }
        }else{
            LazyColumn(
                contentPadding = PaddingValues(4.dp),
                modifier = modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .weight(1f)
            ){
                items(menuList) { menu ->
                    MenuListComponent(
                        id = menu.id,
                        nama = menu.nama,
                        harga = menu.harga,
                        qty = menu.qty,
                        onAdd = {
                            viewModel.modifyQty(menu.id, menu.qty, menu.harga, +1)
                        },
                        onMinus = {
                            if (menu.qty <= 0){
                                Toast.makeText(context, "Jumlah Minimal 0", Toast.LENGTH_SHORT).show()
                            }else{
                                viewModel.modifyQty(menu.id, menu.qty, menu.harga, -1)
                            }
                        },
                        modifier = modifier
                            .padding(bottom = 8.dp)
                            .fillMaxWidth()
                            .animateItemPlacement()
                    )
                }
            }
        }
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.secondary)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Rp ${String.format("%,d", viewModel.subTotal)}",
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "Subtotal",
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
            )
            Button(
                colors = ButtonDefaults.buttonColors(
                    MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation(3.dp),
                modifier = modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                onClick = {
                    if (viewModel.subTotal > 0){
                        viewModel.checkout()
                        context.startActivity(Intent(context, CheckoutActivity::class.java))
                    }else{
                        Toast.makeText(context, "Pilih Menu Minimal 1", Toast.LENGTH_LONG).show()
                    }
                }
            ) {
                Text(text = "Checkout", modifier = modifier.padding(end = 4.dp))
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "", modifier.scale(-1f, 1f))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NanasPOSTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AddScreen()
        }
    }
}