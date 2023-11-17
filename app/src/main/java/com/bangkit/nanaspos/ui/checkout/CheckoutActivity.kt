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
import androidx.compose.material.ButtonColors
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
    var isEdit by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit){
        viewModel.getCheckoutList()
    }

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
        Text(
            text = "Detail Pesanan",
            style = MaterialTheme.typography.labelMedium,
            modifier = modifier.padding(bottom = 8.dp)
        )
        menuList.forEachIndexed { index, menu ->
            CheckoutListComponent(
                nama = menu.nama,
                harga = menu.harga,
                qty = menu.qty,
                subTotal = menu.subTotal,
                isEdit = isEdit
            )
        }
        EditButton(isEdit = isEdit, onClick = { isEdit = !isEdit })

        Spacer(modifier = modifier.padding(20.dp))
        Text(
            text = "Detail Customer",
            style = MaterialTheme.typography.labelMedium,
            modifier = modifier.padding(bottom = 8.dp)
        )
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.secondary)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                label = {
                    Text(text = "Nama Customer", style = MaterialTheme.typography.bodySmall)
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "")
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                    unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSecondary,
                    unfocusedSupportingTextColor = MaterialTheme.colorScheme.onSecondary,
                    focusedLeadingIconColor = MaterialTheme.colorScheme.onSecondary,
                    focusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                    focusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                    textColor = MaterialTheme.colorScheme.onSecondary
                ),
                singleLine = true,
                value = viewModel.customer,
                onValueChange = { viewModel.customer = it },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, top = 4.dp)
            )
        }

        DetailBiayaComponent(viewModel = viewModel)
        
        Button(
            colors = ButtonDefaults.buttonColors(
                MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(8.dp),
            elevation = ButtonDefaults.buttonElevation(3.dp),
            modifier = modifier
                .padding(bottom = 16.dp, top = 16.dp)
                .fillMaxWidth()
                .height(50.dp),
            onClick = {
                if (viewModel.customer == ""){
                    Toast.makeText(context, "Nama Customer Harus Terisi", Toast.LENGTH_SHORT).show()
                }else{
                    viewModel.createTransaction(context)
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

@Composable
fun EditButton(
    isEdit: Boolean,
    onClick: () -> Unit
){
    val btnText = if (isEdit) { "Finish !" } else { "Edit" }
    val btnColor = if (isEdit) { MaterialTheme.colorScheme.primary } else { Color.Green }
    Button(
        onClick = onClick,
        shape = MaterialTheme.shapes.extraSmall,
        colors = ButtonDefaults.buttonColors(
            containerColor = btnColor,
        )
    ) {
        Text(text = btnText)
    }
}

@Composable
fun DetailBiayaComponent(
    viewModel: CheckoutViewModel
){
    val total by viewModel.total.collectAsState()
    val pajak_value by viewModel.pajak_value.collectAsState()

    Text(
        text = "Detail Biaya",
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier.padding(bottom = 8.dp, top = 20.dp)
    )
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.secondary)
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(){
            Text(text="Pajak (10%)", modifier = Modifier.weight(1F))
            Text(text="Rp ${String.format("%, d", pajak_value)}")
        }
        Row(){
            Text(text="Total", modifier = Modifier.weight(1F))
            Text(text="Rp ${String.format("%, d", total)}")
        }
        Row(){
            Text(text="Grand Total", modifier = Modifier.weight(1F))
            Text(text = "Rp ${String.format("%,d", total + pajak_value)}", fontWeight = FontWeight.Bold)
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