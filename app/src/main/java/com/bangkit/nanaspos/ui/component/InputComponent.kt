package com.bangkit.nanaspos.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.widget.Placeholder
import com.bangkit.nanaspos.ui.theme.BGGrayLight
import com.bangkit.nanaspos.ui.theme.Gray
import com.bangkit.nanaspos.ui.theme.Slate

@Composable
fun InputForm(
    modifier: Modifier = Modifier,
    text: String,
    label: String,
    errorText: String,
    placeholder: String = "",
    onValueChange: (it: String) -> Unit,
    leadingIcon: @Composable () -> Unit?
) {
    OutlinedTextField(
        value = text,
        onValueChange = {onValueChange(it)},
        leadingIcon = { leadingIcon() },
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.Black,
            backgroundColor = Color.White,
            focusedBorderColor = Color.LightGray,
            unfocusedBorderColor = Color.Transparent,
        ),
        placeholder = { Text(text = placeholder, color = Slate) },
        modifier = modifier.fillMaxWidth()

    )
}