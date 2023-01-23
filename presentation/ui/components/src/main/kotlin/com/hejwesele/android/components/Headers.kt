package com.hejwesele.android.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HeaderLarge(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier.padding(16.dp),
        text = text,
        style = MaterialTheme.typography.h4
    )
}

@Composable
fun HeaderMedium(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier.padding(16.dp),
        text = text,
        style = MaterialTheme.typography.h5
    )
}

@Composable
fun HeaderSmall(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        text = text,
        style = MaterialTheme.typography.h6
    )
}
