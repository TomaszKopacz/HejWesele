package com.hejwesele.gallery.board

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.hejwesele.android.components.AlertData
import com.hejwesele.android.components.AlertDialog
import com.hejwesele.android.components.ContinuousLottieAnimation
import com.hejwesele.android.components.ErrorData
import com.hejwesele.android.components.ErrorView
import com.hejwesele.android.components.FloatingButton
import com.hejwesele.android.components.HintTile
import com.hejwesele.android.components.Loader
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

    val uiState by viewModel.states.collectAsState()
    val uiEvents by viewModel.events.collectAsState()
    val snackbarState = remember { SnackbarHostState() }

    ImageCropper.install()

    resultRecipient.onNavResult { result ->
        if (result is NavResult.Value && result.value) {
            viewModel.onPhotoUploadSuccess()
        }
    }

    GalleryEventHandler(
        events = uiEvents,
        viewModel = viewModel,
        navigation = navigation,
        snackbarState = snackbarState
    )

    val galleryContentData = with(uiState) {
        GalleryContentData(
            contentActive = contentActive,
            galleryHintVisible = galleryHintEnabled,
            galleryLinkVisible = externalGalleryEnabled,
            photos = photos,
            errorData = errorData,
            alertData = alertData
        )
    }

    val galleryData = GalleryData(
        isLoading = uiState.isLoading,
        isEnabled = uiState.isEnabled,
        contentData = galleryContentData,
        internetPopupEnabled = true
    )

    val galleryActions = GalleryActions(
        onHintDismissed = viewModel::onGalleryHintDismissed,
        onGalleryLinkClicked = viewModel::onGalleryLinkClicked,
        onPhotoClicked = { index -> navigation.openPreview(uiState.photos, index) },
        onAddClicked = viewModel::onAddPhotoClicked
    )

    GalleryBoardScreen(
        data = galleryData,
        actions = galleryActions,
        snackbarState = snackbarState
    )
}

@Composable
private fun GalleryEventHandler(
    events: GalleryUiEvents,
    viewModel: GalleryViewModel,
    navigation: IGalleryNavigation,
    snackbarState: SnackbarHostState
) {
    val context = LocalContext.current
    EventEffect(
        event = events.openExternalGallery,
        onConsumed = viewModel::onExternalGalleryOpened,
        action = { intent -> openActivity(context, intent.intentPackage, intent.intentUrl) }
    )
    EventEffect(
        event = events.openImageCropper,
        onConsumed = { viewModel.onImageCropperOpened() },
        action = { galleryId ->
            ImageCropper.launch(
                onImageCropped = { uri -> navigation.openPhotoConfirmation(uri, galleryId) },
                onImageCropError = viewModel::onImageCropError
            )
        }
    )
    EventEffect(
        event = events.showPhotoUploadSuccess,
        onConsumed = viewModel::onPhotoUploadSuccessShown,
        action = {
            snackbarState.showSnackbar(
                message = Label.galleryPublishingPhotoSuccessText,
                withDismissAction = true
            )
        }
    )
    EventEffect(
        event = events.logout,
        onConsumed = viewModel::onLogout,
        action = navigation::logout
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
    data: GalleryData,
    actions: GalleryActions,
    snackbarState: SnackbarHostState
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
            if (data.internetPopupEnabled) {
                InternetConnectionPopup()
            }
            when {
                data.isLoading -> Loader()
                data.contentData.errorData != null -> ErrorView(modifier = Modifier.fillMaxSize())
                else -> {
                    GalleryBody(
                        modifier = Modifier.fillMaxSize(),
                        isEnabled = data.isEnabled,
                        data = data.contentData,
                        actions = actions
                    )
                }
            }
        }
    }
}

@Composable
private fun GalleryBody(
    modifier: Modifier = Modifier,
    isEnabled: Boolean,
    data: GalleryContentData,
    actions: GalleryActions
) {
    Box(modifier = modifier) {
        if (isEnabled) {
            if (data.contentActive) {
                GalleryContent(
                    photos = data.photos,
                    galleryHintVisible = data.galleryHintVisible,
                    galleryLinkVisible = data.galleryLinkVisible,
                    onHintDismissed = actions.onHintDismissed,
                    onGalleryLinkClicked = actions.onGalleryLinkClicked,
                    onPhotoClicked = actions.onPhotoClicked
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
                enabled = data.contentActive,
                action = { actions.onAddClicked() }
            )
            if (data.alertData != null) {
                AlertDialog(data = data.alertData)
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
    LazyColumn(
        modifier = modifier,
        contentPadding = WindowInsets.statusBars.asPaddingValues()
    ) {
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
    ScrollableColumn(modifier = modifier.statusBarsPadding()) {
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
        Box(modifier = Modifier.weight(Dimension.weightFull)) {
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

private data class GalleryData(
    val isLoading: Boolean,
    val isEnabled: Boolean,
    val contentData: GalleryContentData,
    val internetPopupEnabled: Boolean
) {
    companion object {
        val Preview = GalleryData(
            isLoading = false,
            isEnabled = true,
            contentData = GalleryContentData.Preview,
            internetPopupEnabled = false
        )
    }
}

private data class GalleryContentData(
    val contentActive: Boolean,
    val galleryHintVisible: Boolean,
    val galleryLinkVisible: Boolean,
    val photos: List<String>,
    val errorData: ErrorData?,
    val alertData: AlertData?
) {
    companion object {
        val Preview = GalleryContentData(
            contentActive = true,
            galleryHintVisible = true,
            galleryLinkVisible = true,
            photos = List(50) { "fake photo url" },
            errorData = null,
            alertData = null
        )
    }
}

private data class GalleryActions(
    val onHintDismissed: () -> Unit,
    val onGalleryLinkClicked: () -> Unit,
    val onPhotoClicked: (Int) -> Unit,
    val onAddClicked: () -> Unit
) {
    companion object {
        val Preview = GalleryActions(
            onHintDismissed = {},
            onGalleryLinkClicked = {},
            onPhotoClicked = {},
            onAddClicked = {}
        )
    }
}

@Preview
@Composable
private fun GalleryBoardScreenPreview() {
    val snackbarState = remember { SnackbarHostState() }

    AppTheme(darkTheme = false) {
        GalleryBoardScreen(
            data = GalleryData.Preview,
            actions = GalleryActions.Preview,
            snackbarState = snackbarState
        )
    }
}
