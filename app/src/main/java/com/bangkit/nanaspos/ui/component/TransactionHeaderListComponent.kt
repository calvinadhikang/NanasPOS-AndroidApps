package com.bangkit.nanaspos.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bangkit.nanaspos.ui.theme.NanasPOSTheme

@Composable
fun TransactionHeaderListComponent(
    id: Int,
    status: Int,
    customer: String,
    grandtotal: Int,
    modifier: Modifier = Modifier,
    onClick: (id: Int) -> Unit,
    onDeleteClick: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.secondary
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick(id)
            }
            .padding(bottom = 15.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(12.dp)
        ){
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = customer,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(text = "Rp ${String.format("%,d", grandtotal)}", fontSize = 18.sp)
                badge(status = status)
            }
            IconButton(onClick = {
                onDeleteClick()
            }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "", modifier = Modifier.size(40.dp), tint = Color.Red)
            }
        }
    }
}

@Composable
fun badge(
    status: Int,
    modifier: Modifier = Modifier
){
    if (status == 0){
        Text(
            text = "Belum Lunas",
            color = Color.White,
            modifier = modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Red)
                .padding(6.dp)
        )
    }else{
        Text(
            text = "Lunas",
            color = Color.Black,
            modifier = modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Green)
                .padding(6.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionHeaderListComponentPreview() {
    NanasPOSTheme {
        TransactionHeaderListComponent(
            id = 1,
            status = 1,
            customer = "dikang",
            grandtotal = 80000,
            onClick = {},
            onDeleteClick = {}
        )
    }
}