package com.hejwesele.android.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import com.hejwesele.android.theme.Alpha
import com.hejwesele.android.theme.Dimension

@Composable
fun TextPlaceholder(text: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(Dimension.marginLarge)
            .fillMaxSize()
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.alpha(Alpha.semiTransparent)
        )
    }
}
