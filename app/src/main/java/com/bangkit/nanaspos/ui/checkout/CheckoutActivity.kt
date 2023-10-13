package com.bangkit.nanaspos.ui.checkout

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.checkScrollableContainerConstraints
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.MoneyOffCsred
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.PriceChange
import androidx.compose.material.icons.filled.PriceCheck
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bangkit.nanaspos.ui.component.CheckoutListComponent
import com.bangkit.nanaspos.ui.component.LoadingComponent
import com.bangkit.nanaspos.ui.theme.NanasPOSTheme

class CheckoutActivity() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NanasPOSTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CheckoutScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    viewModel: CheckoutViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val menuList by viewModel.menuList.collectAsState()
    LaunchedEffect(key1 = Unit){
        viewModel.getCheckoutList()
    }

    var customer by rememberSaveable{ mutableStateOf("") }
    var hasDiskon by rememberSaveable { mutableStateOf(false) }
    var hasOngkir by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 10.dp, start = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "",
                modifier = modifier
                    .size(30.dp)
                    .align(Alignment.CenterStart)
                    .clickable {
                        val activity = context as Activity
                        activity.finish()
                    }
            )
            Text(
                text = "Halaman Checkout",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                modifier = modifier.align(Alignment.Center)
            )

        }
        Spacer(modifier = modifier.padding(16.dp))
        menuList.forEachIndexed { index, menu ->
            CheckoutListComponent(
                nama = menu.nama,
                harga = menu.harga,
                qty = menu.qty,
                subTotal = menu.subTotal
            )
        }
        Spacer(modifier = modifier.padding(20.dp))
        Text(
            text = "Detail Pesanan",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(bottom = 8.dp)
        )
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.secondary)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Nama Customer", fontWeight = FontWeight.SemiBold)
            OutlinedTextField(
                leadingIcon = {
                    Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "")
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                    unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSecondary,
                    unfocusedSupportingTextColor = MaterialTheme.colorScheme.onSecondary,
                    focusedLeadingIconColor = MaterialTheme.colorScheme.onSecondary,
                    focusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                    textColor = MaterialTheme.colorScheme.onSecondary
                ),
                singleLine = true,
                value = customer,
                onValueChange = { customer = it },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, top = 4.dp)
            )
        }
        Column(
            modifier = modifier
                .padding(top = 16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.secondary)
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(imageVector = Icons.Default.Discount, contentDescription = "")
                Text(text = "Dapat Diskon ?", fontWeight = FontWeight.SemiBold, modifier = modifier.weight(1F))
                Switch(
                    checked = hasDiskon,
                    onCheckedChange = {
                        hasDiskon = it
                        viewModel.diskon = 0
                        viewModel.countDiscount()
                    }
                )
            }
            if (hasDiskon){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = modifier.padding(bottom = 16.dp)
                ) {
                    TextField(
                        value = viewModel.diskon.toString(),
                        onValueChange = {
                            if (it != ""){
                                var toInt = it.toInt()
                                if (toInt > 100){
                                    toInt = 100
                                }
                                viewModel.diskon = toInt
                            }else{
                                viewModel.diskon = 0
                            }
                            viewModel.countDiscount()
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = modifier
                            .fillMaxWidth(0.2F)
                    )
                    Text(text = "%", fontSize = 15.sp)
                    Text(
                        text = "Total Diskon : Rp ${String.format("%,d", viewModel.diskonTotal)}",
                        textAlign = TextAlign.End,
                        modifier = modifier.weight(1f)
                    )
                }
            }
        }
        Column(
            modifier = modifier
                .padding(top = 16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.secondary)
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(imageVector = Icons.Default.DeliveryDining, contentDescription = "")
                Text(text = "Pesanan Di Kirim ?", fontWeight = FontWeight.SemiBold, modifier = modifier.weight(1F))
                Switch(
                    checked = hasOngkir,
                    onCheckedChange = {
                        hasOngkir = it
                        viewModel.ongkir = 0
                        viewModel.alamat = ""
                    }
                )
            }
            if (hasOngkir){
                Column(
                    modifier = modifier.padding(bottom = 16.dp)
                ) {
                    Text(text = "Harga Ongkir", modifier = modifier.padding(bottom = 4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        TextField(
                            value = viewModel.ongkir.toString(),
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.AttachMoney, contentDescription = "")
                            },
                            onValueChange = {
                                if (it != ""){
                                    viewModel.ongkir = it.toInt()
                                }else{
                                    viewModel.ongkir = 0
                                }
                                viewModel.countDiscount()
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = modifier.fillMaxWidth()
                        )
                    }
                    Text(text = "Alamat Pengiriman", modifier = modifier.padding(bottom = 4.dp))
                    OutlinedTextField(
                        value = viewModel.alamat,
                        onValueChange = {viewModel.alamat = it},
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSecondary,
                            unfocusedSupportingTextColor = MaterialTheme.colorScheme.onSecondary,
                            focusedLeadingIconColor = MaterialTheme.colorScheme.onSecondary,
                            focusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                            textColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        maxLines = 3,
                        modifier = modifier.fillMaxWidth()
                    )
                }
            }
        }
        Spacer(modifier = modifier.padding(20.dp))
        Text(
            text = "Detail Biaya",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(bottom = 8.dp)
        )
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.secondary)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            if (viewModel.diskon > 0 || viewModel.ongkir > 0){
                Row() {
                    Icon(imageVector = Icons.Default.AttachMoney, contentDescription = "")
                    Text(text = "Total Pesanan: ")
                    Text(text = "Rp ${String.format("%,d", viewModel.grandTotal)}", textAlign = TextAlign.End, modifier = modifier.weight(1f))
                }
            }
            if (viewModel.diskon > 0){
                Row(){
                    Icon(imageVector = Icons.Default.Discount, contentDescription = "")
                    Text(text = "Total Diskon: ${viewModel.diskon}%")
                    Text(text = "-Rp ${String.format("%,d", viewModel.diskonTotal)}", textAlign = TextAlign.End, modifier = modifier.weight(1f), color = Color.Red)
                }
            }

            if (hasDiskon || hasOngkir){
                Spacer(modifier = modifier.padding(8.dp))
            }
            Text(text = "Grand Total: ")
            Text(
                text = "Rp ${String.format("%,d", viewModel.finalTotal)}",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(modifier = modifier.padding(16.dp))
        Button(
            colors = ButtonDefaults.buttonColors(
                MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(8.dp),
            elevation = ButtonDefaults.buttonElevation(3.dp),
            modifier = modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .height(50.dp),
            onClick = {
                if (customer == ""){
                    Toast.makeText(context, "Nama Customer Harus Terisi", Toast.LENGTH_SHORT).show()
                }else{
                    viewModel.createTransaction(context, customer)
                }
            }
        ) {
            if (viewModel.isLoading){
                LoadingComponent()
            }else{
                Text(text = "Buat Pesanan", modifier = modifier.padding(end = 4.dp))
                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    NanasPOSTheme {
        CheckoutScreen()
    }
}