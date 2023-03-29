package com.hejwesele.android.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hejwesele.android.theme.AppTheme
import com.hejwesele.android.theme.Dimension

@Composable
fun Loader(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.secondaryContainer,
            strokeWidth = Dimension.progressIndicatorThickness,
            modifier = Modifier.size(Dimension.progressIndicatorSize)
        )
    }
}

@Preview
@Composable
private fun LoaderPreview() {
    AppTheme(darkTheme = false) {
        Loader()
    }
}

@Composable
fun LoaderDialog(
    modifier: Modifier = Modifier,
    label: String?
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            modifier = modifier,
            elevation = Dimension.elevationSmall,
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier.padding(Dimension.marginLarge),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    strokeWidth = Dimension.progressIndicatorThickness,
                    modifier = Modifier.size(Dimension.progressIndicatorSize)
                )
                if (label != null) {
                    HorizontalMargin(Dimension.marginNormal)
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun LoaderDialogPreview() {
    AppTheme(darkTheme = false) {
        LoaderDialog(label = "Loading...")
    }
}
