package com.hejwesele.android.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.hejwesele.android.theme.Dimension

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CircleImage(
    url: String,
    size: Dp,
    offset: Dp = 0.dp
) {
    Surface(
        modifier = Modifier
            .size(size)
            .offset(x = offset)
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
