package com.bangkit.nanaspos.ui.transaction_detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import com.bangkit.nanaspos.ui.component.CreateBadge
import com.bangkit.nanaspos.ui.component.LoadingComponent
import com.bangkit.nanaspos.ui.theme.NanasPOSTheme
import com.bangkit.nanaspos.util.getDateTime
import com.jakewharton.threetenabp.AndroidThreeTen
import java.util.Date


@RequiresApi(Build.VERSION_CODES.O)
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
    val isEdit by viewModel.isEdit.collectAsState()

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
                text = "Detail Transaksi",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.Center)
            )
        }
        Divider()

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
        } else {
            LazyColumn(
                contentPadding = PaddingValues(4.dp, bottom = 40.dp),
                modifier = modifier
                    .padding(bottom = 4.dp)
            ) {
                item {
                    Text(
                        text = "Detail Transaksi",
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

                            Text(text="Tanggal Pesanan", modifier = Modifier.weight(1F))
                            Text(text="${getDateTime(viewModel.htrans.createdAt!!)}")
                        }
                        Row(){
                            Text(text="Status Pesanan", modifier = Modifier.weight(1F))
                            CreateBadge(status = viewModel.htrans.status,)
                        }
                        Row(){
                            Text(text="Customer", modifier = Modifier.weight(1F))
                            Text(text=viewModel.htrans.customer)
                        }
                        Divider(Modifier.padding(vertical = 4.dp))
                        Row(){
                            Text(text="Tax (${viewModel.htrans.tax}%)", modifier = Modifier.weight(1F))
                            Text(text="Rp ${String.format("%,d",viewModel.htrans.tax_value)}")
                        }
                        Row(){
                            Text(text="Total", modifier = Modifier.weight(1F))
                            Text(text="Rp ${String.format("%,d",viewModel.htrans.total)}")
                        }
                        Row(){
                            Text(text="Grand Total", modifier = Modifier.weight(1F), style = MaterialTheme.typography.bodyMedium)
                            Text(text="Rp ${String.format("%,d",viewModel.htrans.grandtotal)}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
                item{
                    Text(
                        text = "Detail Pesanan",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(bottom = 8.dp, top = 20.dp)
                    )
                }
                items(detailList) { trans ->
                    CheckoutListComponent(
                        isEdit = isEdit,
                        nama = trans.nama!!,
                        harga = trans.harga!!,
                        qty = trans.qty!!,
                        subTotal = trans.subtotal!!
                    )
                }
                item {
                    var btnColor = if (isEdit) { MaterialTheme.colorScheme.primary } else { Color.Green }
                    var btnText = if (isEdit) { "Simpan" } else { "Edit" }
                    Button(
                        content = { Text(btnText) },
                        onClick = { viewModel.toggleEdit() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = btnColor
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    if(!isEdit){
                        Text(
                            text = "Selesaikan Pesanan",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(bottom = 8.dp, top = 20.dp)
                        )
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
                                            viewModel.printBluetooth(context)
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
}

@RequiresApi(Build.VERSION_CODES.O)
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