package com.hejwesele.android.components

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.hejwesele.android.theme.Dimension

@OptIn(ExperimentalGlideComposeApi::class)
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
        GlideImage(
            model = url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RoundedCornerImage(
    url: String,
    modifier: Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        modifier = modifier
    ) {
        GlideImage(
            model = url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

// TODO - remove if not used
@Composable
fun RoundedCornerBitmap(
    bitmap: Bitmap,
    modifier: Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        modifier = modifier
    ) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}
