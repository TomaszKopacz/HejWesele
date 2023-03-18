package com.hejwesele.android.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hejwesele.android.theme.Dimension

@Composable
fun Loader() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.secondaryContainer,
            strokeWidth = Dimension.progressIndicatorThickness,
            modifier = Modifier.size(Dimension.progressIndicatorSize)
        )
    }
}

@Composable
fun LoaderDialog(label: String?) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            elevation = Dimension.elevationSmall,
            shape = RoundedCornerShape(Dimension.radiusRoundedCornerSmall),
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
