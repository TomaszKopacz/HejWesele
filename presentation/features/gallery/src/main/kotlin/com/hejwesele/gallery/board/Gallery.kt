package com.hejwesele.gallery.board

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.android.components.ContinuousLottieAnimation
import com.hejwesele.android.components.DismissiveError
import com.hejwesele.android.components.ErrorDialog
import com.hejwesele.android.components.ErrorView
import com.hejwesele.android.components.FloatingButton
import com.hejwesele.android.components.HintTile
import com.hejwesele.android.components.Loader
import com.hejwesele.android.components.PermanentError
import com.hejwesele.android.components.RoundedCornerImage
import com.hejwesele.android.components.TextPlaceholder
import com.hejwesele.android.components.VerticalMargin
import com.hejwesele.android.components.layouts.ScrollableColumn
import com.hejwesele.android.components.layouts.gridItems
import com.hejwesele.android.components.layouts.margin
import com.hejwesele.android.components.layouts.singleItem
import com.hejwesele.android.theme.AppTheme
import com.hejwesele.android.theme.Dimension
import com.hejwesele.android.theme.Label
import com.hejwesele.android.tools.ImageCropper
import com.hejwesele.extensions.openActivity
import com.hejwesele.gallery.IGalleryNavigation
import com.hejwesele.gallery.R
import com.hejwesele.gallery.destinations.PhotoConfirmationDestination
import com.hejwesele.internet.InternetConnectionPopup
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.OpenResultRecipient
import com.ramcosta.composedestinations.result.ResultRecipient
import de.palm.composestateevents.EventEffect
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
fun Gallery(
    navigation: IGalleryNavigation,
    resultRecipient: ResultRecipient<PhotoConfirmationDestination, Boolean>
) {
    GalleryEntryPoint(navigation, resultRecipient)
}

@Composable
private fun GalleryEntryPoint(
    navigation: IGalleryNavigation,
    resultRecipient: OpenResultRecipient<Boolean>,
    viewModel: GalleryViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = Transparent, darkIcons = true)
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

    GalleryEventHandler(
        uiState = uiState,
        snackbarState = snackbarState,
        viewModel = viewModel,
        navigation = navigation,
        context = context
    )

    val galleryData = with(uiState) {
        GalleryData(
            shouldShowContent = weddingStarted,
            galleryHintVisible = galleryHintEnabled,
            galleryLinkVisible = externalGalleryEnabled,
            photos = photos,
            permanentError = permanentError,
            dismissiveError = dismissiveError,
            onHintDismissed = { viewModel.onGalleryHintDismissed() },
            onGalleryLinkClicked = { viewModel.onGalleryLinkClicked() },
            onPhotoClicked = { index -> navigation.openPreview(photos, index) },
            onAddClicked = { viewModel.onAddPhotoClicked() }
        )
    }

    GalleryBoardScreen(
        isEnabled = uiState.isEnabled,
        isLoading = uiState.isLoading,
        isError = uiState.permanentError != null,
        galleryData = galleryData,
        snackbarState = snackbarState,
        internetPopupEnabled = true
    )
}

@Composable
private fun GalleryEventHandler(
    uiState: GalleryUiState,
    viewModel: GalleryViewModel,
    context: Context,
    navigation: IGalleryNavigation,
    snackbarState: SnackbarHostState
) {
    EventEffect(
        event = uiState.openExternalGallery,
        onConsumed = { viewModel.onExternalGalleryOpened() },
        action = { intent -> openActivity(context, intent.intentPackage, intent.intentUrl) }
    )
    EventEffect(
        event = uiState.openImageCropper,
        onConsumed = { viewModel.onImageCropperOpened() },
        action = { galleryId ->
            ImageCropper.launch(
                onImageCropped = { uri -> navigation.openPhotoConfirmation(uri, galleryId) },
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
    EventEffect(
        event = uiState.openLogin,
        onConsumed = { viewModel.onLoginOpened() },
        action = { navigation.openLogin() }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class,
    ExperimentalCoroutinesApi::class
)
@Composable
private fun GalleryBoardScreen(
    isEnabled: Boolean,
    isLoading: Boolean,
    isError: Boolean,
    galleryData: GalleryData,
    snackbarState: SnackbarHostState,
    internetPopupEnabled: Boolean
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarState,
                modifier = Modifier.offset(y = Dimension.marginOutsizeLarge)
            )
        }
    ) {
        Column {
            if (internetPopupEnabled) {
                InternetConnectionPopup()
            }
            when {
                isLoading -> Loader()
                isError -> ErrorView(modifier = Modifier.fillMaxSize())
                else -> {
                    GalleryBody(
                        modifier = Modifier.fillMaxSize(),
                        galleryEnabled = isEnabled,
                        galleryData = galleryData
                    )
                }
            }
        }
    }
}

@Composable
private fun GalleryBody(
    modifier: Modifier = Modifier,
    galleryEnabled: Boolean,
    galleryData: GalleryData
) = with(galleryData) {
    Box(modifier = modifier) {
        if (galleryEnabled) {
            if (shouldShowContent) {
                GalleryContent(
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
            FloatingButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        end = Dimension.marginSmall,
                        bottom = Dimension.marginSmall
                    ),
                icon = Icons.Default.Add,
                enabled = shouldShowContent,
                action = { onAddClicked() }
            )
            if (dismissiveError != null) {
                ErrorDialog(error = dismissiveError)
            }
        } else {
            TextPlaceholder(text = Label.galleryDisabledMessageText)
        }
    }
}

@Composable
private fun GalleryContent(
    modifier: Modifier = Modifier,
    photos: List<String>,
    galleryHintVisible: Boolean,
    galleryLinkVisible: Boolean,
    onHintDismissed: () -> Unit,
    onGalleryLinkClicked: () -> Unit,
    onPhotoClicked: (Int) -> Unit
) {
    if (photos.isNotEmpty()) {
        PopulatedGalleryContent(
            modifier = modifier.fillMaxWidth(),
            photos = photos,
            galleryHintVisible = galleryHintVisible,
            galleryLinkVisible = galleryLinkVisible,
            onHintDismissed = onHintDismissed,
            onGalleryLinkClicked = onGalleryLinkClicked,
            onPhotoClicked = onPhotoClicked
        )
    } else {
        EmptyGalleryContent(
            modifier = modifier,
            galleryHintVisible = galleryHintVisible,
            galleryLinkVisible = galleryLinkVisible,
            onHintDismissed = onHintDismissed,
            onGalleryLinkClicked = onGalleryLinkClicked
        )
    }
}

@Composable
private fun PopulatedGalleryContent(
    modifier: Modifier = Modifier,
    photos: List<String>,
    galleryHintVisible: Boolean,
    galleryLinkVisible: Boolean,
    onHintDismissed: () -> Unit,
    onGalleryLinkClicked: () -> Unit,
    onPhotoClicked: (Int) -> Unit
) {
    LazyColumn(modifier = modifier) {
        margin(Dimension.marginSmall)
        if (galleryHintVisible) {
            singleItem {
                HintTile(
                    modifier = Modifier
                        .padding(horizontal = Dimension.marginNormal)
                        .fillMaxWidth(),
                    text = Label.galleryExternalGalleryHintText,
                    onCloseClick = onHintDismissed
                )
            }
            margin(Dimension.marginNormal)
        }
        if (galleryLinkVisible) {
            singleItem {
                GalleryTile(
                    modifier = Modifier
                        .padding(horizontal = Dimension.marginNormal)
                        .fillMaxWidth(),
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
                        shape = MaterialTheme.shapes.large
                    )
                    .clickable { onPhotoClicked(index) }
            )
        }
        margin(Dimension.marginLarge)
    }
}

@Composable
private fun EmptyGalleryContent(
    modifier: Modifier = Modifier,
    galleryHintVisible: Boolean,
    galleryLinkVisible: Boolean,
    onHintDismissed: () -> Unit,
    onGalleryLinkClicked: () -> Unit
) {
    ScrollableColumn(modifier = modifier) {
        VerticalMargin(Dimension.marginSmall)
        if (galleryHintVisible) {
            HintTile(
                modifier = Modifier
                    .padding(horizontal = Dimension.marginNormal)
                    .fillMaxWidth(),
                text = Label.galleryExternalGalleryHintText,
                onCloseClick = onHintDismissed
            )
            VerticalMargin(Dimension.marginNormal)
        }
        if (galleryLinkVisible) {
            GalleryTile(
                modifier = Modifier
                    .padding(horizontal = Dimension.marginNormal)
                    .fillMaxWidth(),
                onClick = onGalleryLinkClicked
            )
            VerticalMargin(Dimension.marginNormal)
        }
        Box(modifier = Modifier.weight(1.0f)) {
            TextPlaceholder(text = Label.galleryEmptyGalleryPlaceholderText)
        }
    }
}

@Composable
private fun GalleryTile(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = Dimension.elevationSmall,
                shape = MaterialTheme.shapes.small
            )
            .clickable { onClick() },
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier
                .padding(Dimension.marginNormal)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ContinuousLottieAnimation(
                animationResId = R.raw.lottie_gallery,
                size = 48.dp
            )
            Text(
                text = Label.galleryExternalGalleryTileText,
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

@Preview
@Composable
private fun GalleryBoardScreenPreview() {
    val snackbarState = remember { SnackbarHostState() }

    AppTheme(darkTheme = false) {
        GalleryBoardScreen(
            isEnabled = true,
            isLoading = false,
            isError = false,
            galleryData = GalleryData.preview,
            snackbarState = snackbarState,
            internetPopupEnabled = false
        )
    }
}

private data class GalleryData(
    val shouldShowContent: Boolean,
    val galleryHintVisible: Boolean,
    val galleryLinkVisible: Boolean,
    val photos: List<String>,
    val permanentError: PermanentError?,
    val dismissiveError: DismissiveError?,
    val onHintDismissed: () -> Unit,
    val onGalleryLinkClicked: () -> Unit,
    val onPhotoClicked: (Int) -> Unit,
    val onAddClicked: () -> Unit
) {
    companion object {
        val preview = GalleryData(
            shouldShowContent = true,
            galleryHintVisible = true,
            galleryLinkVisible = true,
            photos = List(50) { "fake photo url" },
            permanentError = null,
            dismissiveError = null,
            onHintDismissed = {},
            onGalleryLinkClicked = {},
            onPhotoClicked = {},
            onAddClicked = {}
        )
    }
}
