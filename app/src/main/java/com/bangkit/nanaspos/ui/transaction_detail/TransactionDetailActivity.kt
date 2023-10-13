package com.bangkit.nanaspos.ui.transaction_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bangkit.nanaspos.ui.component.CheckoutListComponent
import com.bangkit.nanaspos.ui.component.badge
import com.bangkit.nanaspos.ui.theme.NanasPOSTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetail(
    key: Int,
    navigateBack: () -> Unit,
    viewModel: TransactionDetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val detailList by viewModel.detailList.collectAsState()

    LaunchedEffect(key1 = Unit){
        viewModel.getTransactionDetail(key)
    }

    Column(
        modifier = modifier.padding(start = 16.dp, end = 16.dp)
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "",
                modifier = modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterStart)
                    .clickable {
                        navigateBack()
                    }
            )
            Text(
                text = viewModel.htrans.customer,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.Center)
            )
        }
        Text(
            text = viewModel.responseMessage,
            textAlign = TextAlign.Center,
            color = Color.DarkGray,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
        )
        if (viewModel.loading){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = viewModel.responseMessage,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                CircularProgressIndicator(
                    strokeWidth = 10.dp,
                    modifier = modifier
                        .fillMaxSize(0.5f)
                )
            }
        }else {
            LazyColumn(
                contentPadding = PaddingValues(4.dp, bottom = 40.dp),
                modifier = modifier
                    .padding(bottom = 16.dp)
            ) {
                items(detailList) { trans ->
                    CheckoutListComponent(
                        nama = trans.nama!!,
                        harga = trans.harga!!,
                        qty = trans.qty!!,
                        subTotal = trans.subtotal!!
                    )
                }
                item {
                    Column(
                        modifier = modifier
                            .clip(RoundedCornerShape(8.dp))
                            .shadow(4.dp)
                            .background(Color.LightGray)
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        //Hitung Kembalian
                        Text(text = "Hitung Kembalian")
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = viewModel.uang.toString(),
                                onValueChange = {
                                    if (it != "") {
                                        viewModel.uang = it.toInt()
                                    } else {
                                        viewModel.uang = 0
                                    }
                                    viewModel.countKembalian()
                                },
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                modifier = modifier
                                    .weight(0.5F)
                                    .padding(end = 4.dp)
                            )
                            Text(
                                text = "Kembali Rp : ${String.format("%,d", viewModel.kembalian)}",
                                fontSize = 20.sp,
                                modifier = modifier.weight(1F)
                            )
                        }
                        Spacer(modifier = modifier.padding(8.dp))
                        Row(
                            modifier = modifier.padding(top = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            badge(
                                status = viewModel.htrans.status,
                            )
                            Spacer(
                                modifier = modifier.weight(1F)
                            )
                            if (viewModel.htrans.status == 0) {
                                Button(
                                    onClick = {
                                        //Finish Pembayaran
                                        viewModel.finishTransaction(key, context)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = ""
                                    )
                                    Spacer(modifier = modifier.padding(4.dp))
                                    Text(text = "Konfirmasi Pembayaran")
                                }
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Total Rp : ${
                                    String.format(
                                        "%,d",
                                        viewModel.htrans.grandtotal
                                    )
                                }",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = modifier.weight(1F)
                            )
                            if (viewModel.printAble) {
                                Button(
                                    onClick = {
                                        //doPrint
                                        viewModel.printBluetooth()
                                    }
                                ) {
                                    Icon(imageVector = Icons.Default.Print, contentDescription = "")
                                    Spacer(modifier = modifier.padding(4.dp))
                                    Text(text = "Print")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun TransactionDetailPreview() {
    NanasPOSTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            TransactionDetail(
                key = 1,
                {}
            )
        }
    }
}