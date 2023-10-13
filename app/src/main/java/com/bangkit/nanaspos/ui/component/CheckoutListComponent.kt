package com.bangkit.nanaspos.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bangkit.nanaspos.ui.theme.NanasPOSTheme

@Composable
fun CheckoutListComponent(
    nama: String,
    harga: Int,
    qty: Int,
    subTotal: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ){
        Column(
            modifier = modifier.weight(1F)
        ) {
            Text(
                text = nama,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = modifier
            )
            Text(
                text = "Rp ${String.format("%,d", harga)}",
                fontSize = 12.sp,
                modifier = modifier
                    .padding(bottom = 8.dp)
            )
            Text(
                text = "Subtotal: Rp ${String.format("%,d", subTotal)}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier.padding(end = 8.dp)
        ) {
            Text(text = "$qty x", fontSize = 28.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckoutListComponentPreview() {
    NanasPOSTheme {
        CheckoutListComponent(
            "Nasi Betutu",
            25000,
            2,
            50000,
        )
    }
}