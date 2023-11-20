package com.bangkit.nanaspos.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bangkit.nanaspos.ui.theme.NanasPOSTheme

@Composable
fun RoundedContainer(
    modifier: Modifier = Modifier,
    color: Color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
    content: @Composable() (ColumnScope.() -> Unit)
){
    Column(
        modifier = modifier
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color)
            .padding(horizontal = 8.dp, vertical = 14.dp)
            .fillMaxWidth()
    ){
        content()
    }
}

@Preview()
@Composable
fun RoundedContainerComponentPreview() {
    NanasPOSTheme {
        RoundedContainer(){
            Text(text = "testing")
        }
    }
}