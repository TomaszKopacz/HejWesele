package com.miquido.android.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Footer(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier.padding(16.dp),
        text = text,
        style = MaterialTheme.typography.body2,
        color = Color.Gray
    )
}
