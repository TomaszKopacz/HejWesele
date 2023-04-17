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
import com.hejwesele.android.theme.AppTheme
import com.hejwesele.android.theme.Dimension
import com.hejwesele.imageloader.CachedImage

@Composable
fun RectangleImage(
    modifier: Modifier = Modifier,
    url: String,
    loader: @Composable () -> Unit = { Loader() },
    fallback: @Composable () -> Unit = { Loader() }
) {
    CachedImage(
        modifier = modifier,
        url = url,
        loader = { loader() },
        fallback = { fallback() },
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun CircleImage(
    modifier: Modifier,
    url: String,
    loader: @Composable () -> Unit = { Loader() },
    fallback: @Composable () -> Unit = { Loader() }
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
        CachedImage(
            modifier = Modifier.fillMaxSize(),
            url = url,
            loader = { loader() },
            fallback = { fallback() }
        )
    }
}

@Composable
fun RoundedCornerImage(
    modifier: Modifier,
    url: String,
    loader: @Composable () -> Unit = { Loader() },
    fallback: @Composable () -> Unit = { Loader() }
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        modifier = modifier
    ) {
        CachedImage(
            modifier = Modifier.fillMaxSize(),
            url = url,
            loader = { loader() },
            fallback = { fallback() }
        )
    }
}

@Preview
@Composable
private fun RectangleImagePreview() {
    AppTheme(darkTheme = false) {
        RectangleImage(
            modifier = Modifier.size(100.dp),
            url = "fake_url"
        )
    }
}

@Preview
@Composable
private fun CircleImagePreview() {
    AppTheme(darkTheme = false) {
        CircleImage(
            modifier = Modifier.size(100.dp),
            url = "fake_url"
        )
    }
}

@Preview
@Composable
private fun RoundedCornerImagePreview() {
    AppTheme(darkTheme = false) {
        RoundedCornerImage(
            modifier = Modifier.size(100.dp),
            url = "fake_url"
        )
    }
}
