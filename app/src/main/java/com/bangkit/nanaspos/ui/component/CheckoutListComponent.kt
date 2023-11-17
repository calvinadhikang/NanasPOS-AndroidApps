package com.bangkit.nanaspos.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
    isEdit: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = Modifier.padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        ){
            Column(
                modifier = modifier.weight(1F)
            ) {
                Text(text = nama,style = MaterialTheme.typography.bodySmall)
                Text(text = "Rp ${String.format("%,d", harga)}")
                Text(text = "Subtotal: Rp ${String.format("%,d", subTotal)}")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier.padding(end = 8.dp)
            ) {
                if (isEdit){
                    IconButton(onClick = {  }) {
                        Icon(imageVector = Icons.Default.RemoveCircle, contentDescription = "")
                    }
                }
                Text(text = "$qty", style = MaterialTheme.typography.bodyMedium)
                if (isEdit){
                    IconButton(onClick = {  }) {
                        Icon(imageVector = Icons.Default.AddCircle, contentDescription = "")
                    }
                }
            }
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