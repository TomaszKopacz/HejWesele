package com.hejwesele.gallery.board

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.android.components.ErrorDialog
import com.hejwesele.android.components.ErrorView
import com.hejwesele.android.components.Loader
import com.hejwesele.android.components.RoundedCornerImage
import com.hejwesele.android.components.TextPlaceholder
import com.hejwesele.android.components.layouts.ScrollableColumn
import com.hejwesele.android.components.layouts.gridItems
import com.hejwesele.android.components.layouts.margin
import com.hejwesele.android.components.layouts.singleItem
import com.hejwesele.android.theme.Dimension
import com.hejwesele.android.theme.Label
import com.hejwesele.android.tools.ImageCropper
import com.hejwesele.extensions.disabled
import com.hejwesele.extensions.openActivity
import com.hejwesele.gallery.IGalleryNavigation
import com.hejwesele.gallery.R
import com.hejwesele.gallery.board.resources.Strings
import com.hejwesele.gallery.destinations.PhotoConfirmationDestination
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.OpenResultRecipient
import com.ramcosta.composedestinations.result.ResultRecipient
import de.palm.composestateevents.EventEffect

@Composable
fun Gallery(
    navigation: IGalleryNavigation,
    resultRecipient: ResultRecipient<PhotoConfirmationDestination, Boolean>
) {
    GalleryBoardScreen(navigation, resultRecipient)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GalleryBoardScreen(
    navigation: IGalleryNavigation,
    resultRecipient: OpenResultRecipient<Boolean>,
    viewModel: GalleryViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Transparent,
            darkIcons = true
        )
    }

    val context = LocalContext.current
    val uiState by viewModel.states.collectAsState()
    val snackbarState = remember { SnackbarHostState() }

    ImageCropper.install()

    resultRecipient.onNavResult { result ->
        if (result is NavResult.Value && result.value) {
            viewModel.onPhotoUploadSuccess()
        }
    }

    EventEffect(
        event = uiState.openExternalGallery,
        onConsumed = { viewModel.onExternalGalleryOpened() },
        action = { intent ->
            openActivity(context, intent.intentPackage, intent.intentUrl)
        }
    )
    EventEffect(
        event = uiState.openImageCropper,
        onConsumed = { viewModel.onImageCropperOpened() },
        action = { galleryId ->
            ImageCropper.launch(
                onImageCropped = { uri ->
                    navigation.openPhotoConfirmation(
                        photoUri = uri,
                        galleryId = galleryId
                    )
                },
                onImageCropError = { viewModel.onImageCropError() }
            )
        }
    )
    EventEffect(
        event = uiState.showPhotoUploadSuccess,
        onConsumed = { viewModel.onPhotoUploadSuccessShown() },
        action = {
            snackbarState.showSnackbar(
                message = Label.galleryPublishingPhotoSuccessText,
                withDismissAction = true
            )
        }
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarState,
                modifier = Modifier.offset(y = Dimension.marginLarge2X)
            )
        }
    ) { padding ->
        with(uiState) {
            if (loading) {
                Loader()
            } else if (error != null) {
                ErrorView(
                    onRetry = { viewModel.onErrorRetry() }
                )
            } else {
                val galleryData = GalleryData(
                    shouldShowContent = weddingStarted,
                    galleryHintVisible = galleryHintEnabled,
                    galleryLinkVisible = externalGalleryEnabled,
                    photos = photos,
                    imageCropFailure = imageCropFailure,
                    onHintDismissed = { viewModel.onGalleryHintDismissed() },
                    onGalleryLinkClicked = { viewModel.onGalleryLinkClicked() },
                    onPhotoClicked = { index -> navigation.openPreview(photos, index) },
                    onAddClicked = { viewModel.onAddPhotoClicked() },
                    onImageCropFailureDismissed = { viewModel.onImageCropErrorDismissed() }
                )
                GalleryBody(
                    padding = padding,
                    galleryEnabled = enabled,
                    galleryData = galleryData
                )
            }
        }
    }
}

@Composable
private fun GalleryBody(
    padding: PaddingValues,
    galleryEnabled: Boolean,
    galleryData: GalleryData
) = with (galleryData) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (galleryEnabled) {
            if (shouldShowContent) {
                GalleryContent(
                    padding = padding,
                    photos = photos,
                    galleryHintVisible = galleryHintVisible,
                    galleryLinkVisible = galleryLinkVisible,
                    onHintDismissed = onHintDismissed,
                    onGalleryLinkClicked = onGalleryLinkClicked,
                    onPhotoClicked = onPhotoClicked
                )
            } else {
                TextPlaceholder(text = Label.galleryBeforeWeddingPlaceholderText)
            }
            FloatingAddButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        end = Dimension.marginSmall,
                        bottom = Dimension.marginSmall
                    ),
                enabled = shouldShowContent,
                action = { onAddClicked() }
            )
            if (imageCropFailure) {
                ErrorDialog(
                    description = Label.galleryCropPhotoErrorDescription,
                    onDismiss = onImageCropFailureDismissed
                )
            }
        } else {
            TextPlaceholder(text = Strings.galleryDisabledMessageText)
        }
    }
}

@Composable
private fun GalleryContent(
    padding: PaddingValues,
    photos: List<String>,
    galleryHintVisible: Boolean,
    galleryLinkVisible: Boolean,
    onHintDismissed: () -> Unit,
    onGalleryLinkClicked: () -> Unit,
    onPhotoClicked: (Int) -> Unit
) {
    if (photos.isNotEmpty()) {
        PopulatedGalleryContent(
            padding = padding,
            photos = photos,
            galleryHintVisible = galleryHintVisible,
            galleryLinkVisible = galleryLinkVisible,
            onHintDismissed = onHintDismissed,
            onGalleryLinkClicked = onGalleryLinkClicked,
            onPhotoClicked = onPhotoClicked
        )
    } else {
        EmptyGalleryContent(
            padding = padding,
            galleryHintVisible = galleryHintVisible,
            galleryLinkVisible = galleryLinkVisible,
            onHintDismissed = onHintDismissed,
            onGalleryLinkClicked = onGalleryLinkClicked
        )
    }
}

@Composable
private fun PopulatedGalleryContent(
    padding: PaddingValues,
    photos: List<String>,
    galleryHintVisible: Boolean,
    galleryLinkVisible: Boolean,
    onHintDismissed: () -> Unit,
    onGalleryLinkClicked: () -> Unit,
    onPhotoClicked: (Int) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        margin(padding.calculateTopPadding() + Dimension.marginSmall)
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
                GalleryTile(
                    onClick = onGalleryLinkClicked
                )
            }
            margin(Dimension.marginNormal)
        }
        gridItems(
            items = photos,
            columnCount = 3,
            padding = Dimension.gridPaddingThin,
            innerPadding = Dimension.gridPaddingThin
        ) { index ->
            RoundedCornerImage(
                url = photos[index],
                modifier = Modifier
                    .shadow(
                        elevation = Dimension.elevationSmall,
                        shape = RoundedCornerShape(Dimension.radiusRoundedCornerSmall)
                    )
                    .clickable { onPhotoClicked(index) }
            )
        }
        margin(Dimension.marginLarge)
    }
}

@Composable
private fun EmptyGalleryContent(
    padding: PaddingValues,
    galleryHintVisible: Boolean,
    galleryLinkVisible: Boolean,
    onHintDismissed: () -> Unit,
    onGalleryLinkClicked: () -> Unit
) {
    ScrollableColumn {
        Spacer(modifier = Modifier.height(padding.calculateTopPadding() + Dimension.marginSmall))
        if (galleryHintVisible) {
            GalleryHintTile(
                onCloseClick = onHintDismissed
            )
            Spacer(modifier = Modifier.height(Dimension.marginNormal))
        }
        if (galleryLinkVisible) {
            GalleryTile(
                onClick = onGalleryLinkClicked
            )
            Spacer(modifier = Modifier.height(Dimension.marginNormal))
        }
        Box(modifier = Modifier.weight(1.0f)) {
            TextPlaceholder(text = Label.galleryEmptyGalleryPlaceholderText)
        }
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
private fun GalleryTile(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .padding(horizontal = Dimension.marginNormal)
            .fillMaxWidth()
            .shadow(
                elevation = Dimension.elevationSmall,
                shape = RoundedCornerShape(Dimension.radiusRoundedCornerNormal)
            )
            .clickable { onClick() },
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
    enabled: Boolean,
    action: () -> Unit
) {
    val iconColor = MaterialTheme.colorScheme.onTertiaryContainer

    Surface(
        modifier = modifier
            .shadow(
                elevation = Dimension.elevationSmall,
                shape = RoundedCornerShape(Dimension.radiusRoundedCornerSmall)
            )
            .clickable(enabled = enabled) { action() },
        color = MaterialTheme.colorScheme.tertiaryContainer,
        shape = RoundedCornerShape(Dimension.radiusRoundedCornerSmall)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = if (enabled) iconColor else iconColor.disabled,
            modifier = Modifier.padding(Dimension.marginNormal)
        )
    }
}

private data class GalleryData(
    val shouldShowContent: Boolean,
    val galleryHintVisible: Boolean,
    val galleryLinkVisible: Boolean,
    val photos: List<String>,
    val imageCropFailure: Boolean,
    val onHintDismissed: () -> Unit,
    val onGalleryLinkClicked: () -> Unit,
    val onPhotoClicked: (Int) -> Unit,
    val onAddClicked: () -> Unit,
    val onImageCropFailureDismissed: () -> Unit
)
