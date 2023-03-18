package com.hejwesele.android.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun VerticalMargin(height: Dp) {
    Spacer(modifier = Modifier.height(height))
}

@Composable
fun HorizontalMargin(height: Dp) {
    Spacer(modifier = Modifier.width(height))
}