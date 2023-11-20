package com.bangkit.nanaspos.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bangkit.nanaspos.ui.theme.LightGreen
import com.bangkit.nanaspos.ui.theme.Rose

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBadge(
    status: Int,
    modifier: Modifier = Modifier
){
    val text = if (status == 0) { "Belum Lunas" } else { "Lunas" }
    val color = if (status == 0) { Rose } else { LightGreen }

    Badge(
        containerColor = color,
        contentColor = Color.White
    ) {
        Text(text, modifier = modifier.padding(4.dp))
    }
}