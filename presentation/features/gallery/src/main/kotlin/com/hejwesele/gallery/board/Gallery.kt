package com.hejwesele.gallery.board

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.android.components.RoundedCornerImage
import com.hejwesele.android.components.TextPlaceholder
import com.hejwesele.android.components.layouts.gridItems
import com.hejwesele.android.components.layouts.margin
import com.hejwesele.android.components.layouts.singleItem
import com.hejwesele.android.theme.Dimension
import com.hejwesele.android.tools.ImageCropper
import com.hejwesele.gallery.IGalleryNavigation
import com.hejwesele.gallery.R
import com.hejwesele.gallery.board.GalleryUiAction.OpenImageCropperWithConfirmation
import com.hejwesele.gallery.board.resources.Strings

@Composable
fun Gallery(
    navigation: IGalleryNavigation
) {
    GalleryBoardScreen(navigation)
}

@Composable
private fun GalleryBoardScreen(
    navigation: IGalleryNavigation,
    viewModel: GalleryViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Transparent,
            darkIcons = true
        )
    }

    val uiState by viewModel.states.collectAsState()

    ImageCropper.install()

    LaunchedEffect(uiState.action) {
        when (val action = uiState.action) {
            is OpenImageCropperWithConfirmation -> ImageCropper.launch(
                onImageCropped = { uri ->
                    navigation.openPhotoConfirmation(
                        photoUri = uri,
                        galleryId = action.galleryId
                    )
                },
                onImageCropError = { /* TODO - show error */ }
            )
            null -> {}
        }
        viewModel.onActionConsumed()
    }

    GalleryContent(
        galleryEnabled = uiState.enabled,
        photos = uiState.photos,
        galleryHintVisible = uiState.galleryHintVisible,
        galleryLinkVisible = uiState.galleryLinkVisible,
        onHintDismissed = { viewModel.onGalleryHintDismissed() },
        onPhotoClicked = { photo -> navigation.openPreview(photo) },
        onAddClicked = { viewModel.onAddPhotoClicked() },
    )
}

@Composable
private fun GalleryContent(
    galleryEnabled: Boolean,
    photos: List<String>,
    galleryHintVisible: Boolean,
    galleryLinkVisible: Boolean,
    onHintDismissed: () -> Unit,
    onPhotoClicked: (String) -> Unit,
    onAddClicked: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (galleryEnabled) {
            ScrollableContent(
                photos = photos,
                galleryHintVisible = galleryHintVisible,
                galleryLinkVisible = galleryLinkVisible,
                onHintDismissed = onHintDismissed,
                onPhotoClicked = onPhotoClicked
            )
            FloatingAddButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        end = Dimension.marginSmall,
                        bottom = Dimension.marginSmall
                    ),
                action = { onAddClicked() }
            )
        } else {
            TextPlaceholder(text = Strings.galleryDisabledMessageText)
        }
    }
}

@Composable
private fun ScrollableContent(
    photos: List<String>,
    galleryHintVisible: Boolean,
    galleryLinkVisible: Boolean,
    onHintDismissed: () -> Unit,
    onPhotoClicked: (String) -> Unit
) {
    val topPadding = WindowInsets.statusBars
        .only(WindowInsetsSides.Top)
        .asPaddingValues()
        .calculateTopPadding()

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        margin(topPadding + Dimension.marginSmall)
        if (galleryHintVisible) {
            singleItem {
                GalleryHintTile(
                    onCloseClick = onHintDismissed
                )
            }
            margin(Dimension.marginNormal)
        }
        if (galleryLinkVisible) {
            singleItem {
                GalleryTile()
            }
            margin(Dimension.marginNormal)
        }
        gridItems(
            items = photos,
            columnCount = 3,
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
                    .clickable { onPhotoClicked("PHOTO ID: 44") }
            )
        }
        margin(Dimension.marginLarge)
    }
}

@Composable
private fun GalleryHintTile(onCloseClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .padding(horizontal = Dimension.marginNormal)
            .fillMaxWidth(),
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
            .padding(horizontal = Dimension.marginNormal)
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
            .size(48.dp)
            .aspectRatio(1.0f)
    )
}

@Composable
private fun FloatingAddButton(
    modifier: Modifier = Modifier,
    action: () -> Unit
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = Dimension.elevationSmall,
                shape = RoundedCornerShape(Dimension.radiusRoundedCornerSmall)
            )
            .clickable { action() },
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
