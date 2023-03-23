package com.hejwesele.android.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.hejwesele.android.theme.AppTheme
import com.hejwesele.android.theme.Dimension

@Composable
fun CircleImage(
    url: String,
    modifier: Modifier
) {
    Surface(
        modifier = modifier
            .border(
                border = BorderStroke(
                    width = Dimension.borderWidthNormal,
                    brush = SolidColor(MaterialTheme.colorScheme.outline)
                ),
                shape = CircleShape
            ),
        shape = CircleShape
    ) {
        SubcomposeAsyncImage(
            model = url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
private fun CircleImagePreview() {
    AppTheme(darkTheme = false) {
        CircleImage(
            url = "fake_url",
            modifier = Modifier.size(100.dp)
        )
    }
}

@Composable
fun RoundedCornerImage(
    url: String,
    modifier: Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        modifier = modifier
    ) {
        SubcomposeAsyncImage(
            model = url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
private fun RoundedCornerImagePreview() {
    AppTheme(darkTheme = false) {
        RoundedCornerImage(
            url = "fake_url",
            modifier = Modifier.size(100.dp)
        )
    }
}
