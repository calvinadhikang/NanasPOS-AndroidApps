package com.bangkit.nanaspos.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bangkit.nanaspos.ui.theme.NanasPOSTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuListComponent(
    id: Int,
    nama: String,
    harga: Int,
    qty: Int,
    modifier: Modifier = Modifier,
    onAdd: (id: Int) -> Unit,
    onMinus: (id: Int) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ){
        Column(
            modifier = Modifier.weight(1F)
        ) {
            Text(
                text = nama,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "Rp ${String.format("%,d", harga)}",
                fontSize = 12.sp,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FilledIconButton(
                onClick = {
                    onMinus(id)
                }
            ) {Icon(imageVector = Icons.Default.Remove, contentDescription = "") }
            Text(text = qty.toString(), fontSize = 28.sp)
            FilledIconButton(
                onClick = {
                    onAdd(id)
                }
            ) {Icon(imageVector = Icons.Default.Add, contentDescription = "")}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuListComponentPreview() {
    NanasPOSTheme {
        MenuListComponent(
            0,
            "Nasi Betutu",
            25000,
            0,
            onAdd = {},
            onMinus = {}
        )
    }
}