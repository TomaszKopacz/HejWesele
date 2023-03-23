package com.hejwesele.android.components

import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.hejwesele.android.theme.AppTheme

@Composable
fun PlainButton(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = MaterialTheme.colorScheme.onBackground,
    style: TextStyle = MaterialTheme.typography.titleLarge,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = style,
            color = color
        )
    }
}

@Preview
@Composable
private fun PlainButtonPreview() {
    AppTheme(darkTheme = false) {
        PlainButton(
            text = "Submit",
            onClick = {}
        )
    }
}
