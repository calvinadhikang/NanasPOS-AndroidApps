package com.bangkit.nanaspos.ui.transaction

import android.R
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.widget.EdgeEffectCompat
import com.bangkit.nanaspos.UserPreference
import com.bangkit.nanaspos.ui.component.TransactionHeaderListComponent
import com.bangkit.nanaspos.ui.theme.NanasPOSTheme

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun Transaction(
    viewModel: TransactionViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    modifier: Modifier = Modifier,
    navigateToDetail: (id: Int) -> Unit
) {
    val context = LocalContext.current
    val pref = UserPreference(context).getUser()
    val menuList by viewModel.transList.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var deleteTransactionId by remember { mutableStateOf(-1)}
    if (showDialog){
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Yakin Ingin Hapus Transaksi?", fontSize = 20.sp, fontWeight = FontWeight.SemiBold) },
            text = { Text("Transaksi yang sudah dihapus tidak dapat dikembalikan!") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteTransaction(deleteTransactionId, pref.divisi)
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {
                    Text("Ya, Hapus".uppercase())
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Tidak, Batalkan".uppercase())
                }
            },
        )
    }


    LaunchedEffect(key1 = Unit){
        viewModel.getTransaction(pref.divisi)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
    ) {
        Text(
            text = "Transaksi Hari Ini",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .padding(bottom = 10.dp)
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
        }else{
            LazyColumn(
                contentPadding = PaddingValues(4.dp),
                modifier = modifier
                    .weight(1f)
            ){
                items(
                    items = menuList,
                    key = { item -> item.id },
                ) { trans ->
                    TransactionHeaderListComponent(
                        id = trans.id,
                        customer = trans.customer,
                        grandtotal = trans.grandtotal,
                        status = trans.status,
                        onClick = {
                            navigateToDetail(trans.id)
                        },
                        onDeleteClick = {
                            showDialog = true
                            deleteTransactionId = trans.id
                        },
                        modifier = modifier.animateItemPlacement()
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
                text = "Rp ${String.format("%,d", viewModel.grandTotal)}",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Total Pendapatan Hari Ini",
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeBackground(
    dismissState: DismissState
){
    val direction = dismissState.dismissDirection ?: return

    val color by animateColorAsState(
        when(dismissState.targetValue){
            DismissValue.Default -> Color.Transparent
            DismissValue.DismissedToStart -> Color.Red
            DismissValue.DismissedToEnd -> Color.Transparent
        }
    )

    val scale by animateFloatAsState(
        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
    )

    Box(
        contentAlignment = Alignment.CenterEnd,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 15.dp)
            .background(color)
            .padding(horizontal = 20.dp)
    ) {
        Icon(imageVector = Icons.Default.Delete, contentDescription = "")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    NanasPOSTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Transaction(navigateToDetail = {})
        }
    }
}