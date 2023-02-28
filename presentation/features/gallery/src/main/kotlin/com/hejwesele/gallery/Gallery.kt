package com.hejwesele.gallery

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.canhub.cropper.CropImageContract
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.android.components.RoundedCornerImage
import com.hejwesele.android.components.layouts.gridItems
import com.hejwesele.android.components.layouts.margin
import com.hejwesele.android.components.layouts.singleItem
import com.hejwesele.android.theme.Dimension
import com.hejwesele.gallery.constants.Numbers
import com.hejwesele.gallery.constants.Strings
import com.hejwesele.gallery.model.GalleryUiAction.OpenDeviceGallery
import com.hejwesele.gallery.model.GalleryUiAction.OpenImageCropper
import com.hejwesele.gallery.navigation.GalleryFeatureNavigation
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.CoroutineContext

@Composable
fun Gallery(
    navigation: GalleryFeatureNavigation
) {
    GalleryScreen(navigation)
}

@Composable
private fun GalleryScreen(
    navigation: GalleryFeatureNavigation,
    viewModel: GalleryViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = true
        )
    }

    val imageCropperLauncher = rememberLauncherForActivityResult(CropImageContract()) { cropResult ->
        viewModel.onImageCropped(cropResult)
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(GetContent()) { uri ->
        viewModel.onImageSelected(uri)
    }

    val uiState by viewModel.states.collectAsState()

    when (val action = uiState.action) {
        is OpenDeviceGallery -> imagePickerLauncher.launch(action.directory)
        is OpenImageCropper -> imageCropperLauncher.launch(action.options)
        null -> { /*no-op*/ }
    }
    viewModel.onActionConsumed()

    GalleryContent(
        photos = uiState.photos,
        galleryHintVisible = uiState.galleryHintVisible,
        galleryLinkVisible = uiState.galleryLinkVisible,
        onHintDismissed = { viewModel.onGalleryHintDismissed() },
        onPhotoClicked = { photo -> navigation.openPreview(photo) },
        onAddClicked = { viewModel.onAddPhotoClicked() },
    )
}

@Composable
internal fun GalleryContent(
    photos: List<String>,
    galleryHintVisible: Boolean,
    galleryLinkVisible: Boolean,
    onHintDismissed: () -> Unit,
    onPhotoClicked: (String) -> Unit,
    onAddClicked: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
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
    }
}

@Composable
internal fun ScrollableContent(
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
            .size(Numbers.galleryAnimationSize)
            .aspectRatio(Numbers.galleryAnimationRatio)
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

@Composable
private fun <T> SharedFlow<T>.CollectAsEffect(
    context: CoroutineContext,
    block: (T) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        this@CollectAsEffect.onEach { block(it) }.flowOn(context).launchIn(this)
    }
}