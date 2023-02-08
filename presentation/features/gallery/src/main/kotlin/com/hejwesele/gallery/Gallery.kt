package com.hejwesele.gallery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.android.components.RoundedCornerImage
import com.hejwesele.android.components.layouts.gridItems
import com.hejwesele.android.components.layouts.margin
import com.hejwesele.android.components.layouts.singleItem
import com.hejwesele.android.theme.Dimension
import com.hejwesele.gallery.constants.Numbers
import com.hejwesele.gallery.constants.Strings
import com.hejwesele.gallery.model.GalleryUiAction.OpenDeviceGallery
import kotlinx.coroutines.launch

@Composable
internal fun Gallery(
    viewModel: GalleryViewModel
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = true
        )
    }

    val coroutineScope = rememberCoroutineScope()

    val uiState by viewModel.states.collectAsState()

    GalleryContent(
        photos = uiState.photos,
        galleryHintVisible = uiState.galleryHintVisible,
        galleryLinkVisible = uiState.galleryLinkVisible,
        onHintDismissed = { viewModel.onGalleryHintDismissed() }
    )

    SideEffect {
        coroutineScope.launch {
            viewModel.actions.collect { action ->
                when (action) {
                    OpenDeviceGallery -> {

                    }
                }
            }
        }
    }
}

@Composable
internal fun GalleryContent(
    photos: List<String>,
    galleryHintVisible: Boolean,
    galleryLinkVisible: Boolean,
    onHintDismissed: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        ScrollableContent(
            photos = photos,
            galleryHintVisible = galleryHintVisible,
            galleryLinkVisible = galleryLinkVisible,
            onHintDismissed = onHintDismissed
        )
        FloatingAddButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = Dimension.marginSmall,
                    bottom = Dimension.marginSmall
                )
        )
    }
}

@Composable
internal fun ScrollableContent(
    photos: List<String>,
    galleryHintVisible: Boolean,
    galleryLinkVisible: Boolean,
    onHintDismissed: () -> Unit
) {
    val topPadding = WindowInsets.statusBars
        .only(WindowInsetsSides.Top)
        .asPaddingValues()
        .calculateTopPadding()

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        margin(topPadding + Dimension.marginSmall)
        if (galleryHintVisible) {
            singleItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimension.marginNormal)
            ) {
                GalleryHintTile(
                    onCloseClick = onHintDismissed
                )
            }
            margin(Dimension.marginNormal)
        }
        if (galleryLinkVisible) {
            singleItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimension.marginNormal)
            ) {
                GalleryTile()
            }
            margin(Dimension.marginNormal)
        }
        gridItems(
            items = photos,
            columnCount = Numbers.galleryColumnCount,
            padding = Dimension.gridPaddingThin,
            innerPadding = Dimension.gridPaddingThin
        ) { url ->
            RoundedCornerImage(
                url = url,
                modifier = Modifier
                    .shadow(
                        elevation = Dimension.elevationSmall,
                        shape = RoundedCornerShape(Dimension.radiusRoundedCornerSmall)
                    )
                    .clickable {
                        // no-op
                    }
            )
        }
        margin(Dimension.marginLarge)
    }
}

@Composable
private fun GalleryHintTile(onCloseClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.tertiaryContainer,
        shape = RoundedCornerShape(Dimension.radiusRoundedCornerNormal)
    ) {
        Row(
            modifier = Modifier
                .padding(Dimension.marginNormal)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_info),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Text(
                text = Strings.galleryHintText,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1f, true)
                    .padding(horizontal = Dimension.marginSmall)
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier.clickable { onCloseClick() }
            )
        }
    }
}

@Composable
private fun GalleryTile() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = Dimension.elevationSmall,
                shape = RoundedCornerShape(Dimension.radiusRoundedCornerNormal)
            )
            .clickable {
                // no-op
            },
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(Dimension.radiusRoundedCornerNormal)
    ) {
        Row(
            modifier = Modifier
                .padding(Dimension.marginNormal)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GalleryLottieAnimation()
            Text(
                text = Strings.galleryTileText,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1f, true)
                    .padding(start = Dimension.marginSmall)
            )
        }
    }
}

@Composable
private fun GalleryLottieAnimation() {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.lottie_gallery))
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier
            .size(Numbers.galleryAnimationSize)
            .aspectRatio(Numbers.galleryAnimationRatio)
    )
}

@Composable
private fun FloatingAddButton(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = Dimension.elevationSmall,
                shape = RoundedCornerShape(Dimension.radiusRoundedCornerSmall)
            )
            .clickable { /* no-op */ },
        color = MaterialTheme.colorScheme.tertiaryContainer,
        shape = RoundedCornerShape(Dimension.radiusRoundedCornerSmall)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.padding(Dimension.marginNormal)
        )
    }
}
