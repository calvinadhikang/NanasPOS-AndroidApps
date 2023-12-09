package com.bangkit.nanaspos.ui.main

import android.content.Intent
import android.widget.Toast
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
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ButtonColors
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.RestartAlt
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bangkit.nanaspos.UserPreference
import com.bangkit.nanaspos.api.ApiConfig
import com.bangkit.nanaspos.factory.ViewModelFactory
import com.bangkit.nanaspos.ui.component.InputForm
import com.bangkit.nanaspos.ui.component.LoadingComponent
import com.bangkit.nanaspos.ui.component.MenuListComponent
import com.bangkit.nanaspos.ui.component.RoundedContainer
import com.bangkit.nanaspos.ui.theme.Brown
import com.bangkit.nanaspos.ui.theme.Gray
import com.bangkit.nanaspos.ui.theme.LightBrown
import com.bangkit.nanaspos.ui.theme.NanasPOSTheme
import com.bangkit.nanaspos.ui.theme.White
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddScreen(
    viewModel: MainViewModel = viewModel(factory = ViewModelFactory(ApiConfig.getApiService(), LocalContext.current)),
    modifier: Modifier = Modifier
) {
    val menuList by viewModel.menuList.collectAsState()
    var searchKey by rememberSaveable { mutableStateOf("") }
    var emptyResult by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(key1 = Unit){
        viewModel.getMenu()
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
        InputForm(text = searchKey, label = "", errorText = "", onValueChange = {searchKey = it},
            leadingIcon = { Icon(
                imageVector = Icons.Default.Search,
                contentDescription = ""
            )},
        )

        Spacer(Modifier.padding(8.dp))

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
                    .weight(1f)
            ){
                item {
                    val result = menuList.filter { menu -> menu.nama.contains(searchKey, ignoreCase = true) }
                    Text(text = "${result.count()} Menu", textAlign = TextAlign.Center,modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp))
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

        Column(
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .background(LightBrown)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Rp ${String.format("%,d", viewModel.subTotal)}",
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = White,
            )
            Text(
                text = "Subtotal",
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                color = Gray
            )
            Button(
                colors = ButtonDefaults.buttonColors(
                    Brown
                ),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation(3.dp),
                modifier = modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                onClick = { viewModel.checkout() }
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