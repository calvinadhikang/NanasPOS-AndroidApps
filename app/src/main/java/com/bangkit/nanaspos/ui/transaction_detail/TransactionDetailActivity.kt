package com.bangkit.nanaspos.ui.transaction_detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bangkit.nanaspos.api.ApiConfig
import com.bangkit.nanaspos.factory.ViewModelFactory
import com.bangkit.nanaspos.ui.component.CheckoutListComponent
import com.bangkit.nanaspos.ui.component.CreateBadge
import com.bangkit.nanaspos.ui.component.InputForm
import com.bangkit.nanaspos.ui.component.LoadingComponent
import com.bangkit.nanaspos.ui.component.MenuListComponent
import com.bangkit.nanaspos.ui.theme.Brown
import com.bangkit.nanaspos.ui.theme.LightBrown
import com.bangkit.nanaspos.ui.theme.NanasPOSTheme
import com.bangkit.nanaspos.ui.theme.SuperLightBrown
import com.bangkit.nanaspos.ui.theme.White
import com.bangkit.nanaspos.util.getDateTime
import kotlinx.coroutines.launch
import java.util.Date


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun TransactionDetail(
    key: Int,
    navigateBack: () -> Unit,
    viewModel: TransactionDetailViewModel = viewModel(factory = ViewModelFactory(api = ApiConfig.getApiService(), context = LocalContext.current)),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val detailList by viewModel.detailList.collectAsState()
    val menuList by viewModel.menuList.collectAsState()
    val isEdit by viewModel.isEdit.collectAsState()
    var sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    var showBottomSheet by rememberSaveable() { mutableStateOf(false) }
    var searchKey by rememberSaveable() { mutableStateOf("") }
    var emptyResult by rememberSaveable() { mutableStateOf(true) }

    LaunchedEffect(key1 = Unit){
        viewModel.getTransactionDetail(key)
    }

    if(showBottomSheet){
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            Modifier.fillMaxHeight(0.8F),
            containerColor = LightBrown
        ) {
            LaunchedEffect(key1 = 2){
                viewModel.getMenu()
            }

            Column(
                Modifier.padding(16.dp)
            ) {
                Text(text = ("Tambah Menu"))
                InputForm(text = searchKey, label = "", errorText = "", onValueChange = {searchKey = it},
                    leadingIcon = { Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = ""
                    )},
                )

                Spacer(Modifier.padding(8.dp))

                if (viewModel.loadingMenu){
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
                            .weight(1F)
                    ){
                        item {
                            val result = menuList.filter { menu -> menu.nama.contains(searchKey, ignoreCase = true) }
                            Text(text = "${result.count()} Menu", textAlign = TextAlign.Center,modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp))
                        }
                        items(menuList) { menu ->
                            if (menu.nama.contains(searchKey, ignoreCase = true)){
                                emptyResult = false
                                MenuListComponent(
                                    id = menu.id,
                                    nama = menu.nama,
                                    harga = menu.harga,
                                    qty = menu.qty,
                                    onAdd = {
                                        viewModel.modifyQty(menu.id, menu.qty, menu.harga, +1)
                                    },
                                    onMinus = {
                                        viewModel.modifyQty(menu.id, menu.qty, menu.harga, -1)
                                    },
                                    modifier = modifier
                                        .padding(bottom = 8.dp)
                                        .fillMaxWidth()
                                        .animateItemPlacement()
                                )
                                Divider()
                            }
                        }
                    }
                }
                Button(onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                }) {
                    Text("Hide bottom sheet")
                }
            }
            Spacer(modifier = Modifier.padding(10.dp))
        }
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
                modifier = modifier.padding(bottom = 4.dp)
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
                            .background(SuperLightBrown)
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
                    if (isEdit){
                        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
                            IconButton(onClick = { showBottomSheet = true }) {
                                Row{
                                    Icon(imageVector = Icons.Default.AddCircle, contentDescription = "", tint = Brown)
                                    Text("Tambah Menu")
                                }
                            }
                        }
                    }
                }
                item {
                    var btnColor = if (isEdit) { MaterialTheme.colorScheme.primary } else { Color.Green }
                    var btnText = if (isEdit) { "Simpan" } else { "Edit" }
                    Button(
                        content = { Text(btnText) },
                        onClick = {
                            viewModel.toggleEdit()
                        },
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
                                .background(SuperLightBrown)
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            //Hitung Kembalian
                            Text(text = "Hitung Kembalian", modifier = Modifier.padding(bottom = 8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
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
                                    shape = RoundedCornerShape(8.dp),
                                    colors = TextFieldDefaults.colors(
                                        unfocusedContainerColor = White,
                                        focusedContainerColor = White,
                                    ),
                                    modifier = modifier
                                        .weight(0.5F)
                                        .padding(end = 4.dp),
                                )
                                Column(
                                    modifier = modifier
                                        .weight(1F)
                                        .padding(start = 16.dp),
                                ){
                                    Text(text = "Kembali", fontSize = 12.sp)
                                    Text(
                                        text = "Rp ${String.format("%,d", viewModel.kembalian)}",
                                        fontSize = 20.sp,
                                    )
                                }
                            }
                            Spacer(modifier = modifier.padding(8.dp))
                            Column {
                                Text(text = "Total")
                                Text(
                                    text = "Rp ${String.format("%,d", viewModel.htrans.grandtotal)}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
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
                        }
                        if (viewModel.printAble) {
                            Button(
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                shape = RoundedCornerShape(8.dp),
                                onClick = {
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