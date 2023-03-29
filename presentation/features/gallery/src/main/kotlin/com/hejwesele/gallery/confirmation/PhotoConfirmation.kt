package com.hejwesele.gallery.confirmation

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.android.components.ErrorDialog
import com.hejwesele.android.components.Loader
import com.hejwesele.android.components.LoaderDialog
import com.hejwesele.android.components.PlainButton
import com.hejwesele.android.components.VerticalMargin
import com.hejwesele.android.components.layouts.BottomSheetScaffold
import com.hejwesele.android.theme.AppTheme
import com.hejwesele.android.theme.Dimension
import com.hejwesele.android.theme.Label
import com.hejwesele.android.theme.md_theme_dark_background
import com.hejwesele.android.theme.md_theme_dark_onBackground
import com.hejwesele.internet.InternetConnectionPopup
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import de.palm.composestateevents.EventEffect
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@Composable
@Destination(navArgsDelegate = PhotoConfirmationNavArgs::class)
fun PhotoConfirmation(
    resultSender: ResultBackNavigator<Boolean>
) {
    PhotoConfirmationEntryPoint(resultSender)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PhotoConfirmationEntryPoint(
    resultSender: ResultBackNavigator<Boolean>,
    viewModel: PhotoConfirmationViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = Color.Transparent, darkIcons = true)
    }

    val coroutineScope = rememberCoroutineScope()

    val uiState by viewModel.states.collectAsState()
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )

    PhotoConfirmationEventHandler(
        uiState = uiState,
        sheetState = sheetState,
        viewModel = viewModel,
        resultSender = resultSender
    )

    val actions = PhotoConfirmationActions(
        onPhotoAccepted = { viewModel.onPhotoAccepted() },
        onPhotoDeclined = { viewModel.onPhotoDeclined() },
        onPhotoConfirmationAccepted = { viewModel.onPhotoConfirmationAccepted() },
        onPhotoConfirmationDeclined = { viewModel.onPhotoConfirmationDeclined() },
        onErrorDismissed = { viewModel.onErrorDismissed() }
    )

    PhotoConfirmationScreen(
        isLoading = uiState.loadingData,
        isError = uiState.error != null,
        photo = uiState.photo,
        isUploadingPhoto = uiState.uploadingPhoto,
        uploadingMessage = uiState.uploadingMessage,
        sheetState = sheetState,
        internetPopupEnabled = true,
        actions = actions
    )

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PhotoConfirmationEventHandler(
    uiState: PhotoConfirmationUiState,
    viewModel: PhotoConfirmationViewModel,
    sheetState: ModalBottomSheetState,
    resultSender: ResultBackNavigator<Boolean>
) {
    EventEffect(
        event = uiState.showPhotoConfirmation,
        onConsumed = { viewModel.onPhotoConfirmationShown() },
        action = { sheetState.show() }
    )
    EventEffect(
        event = uiState.hidePhotoConfirmation,
        onConsumed = { viewModel.onPhotoConfirmationHidden() },
        action = { sheetState.hide() }
    )
    EventEffect(
        event = uiState.closeScreen,
        onConsumed = { viewModel.onScreenClosed() },
        action = { resultSender.navigateBack(result = false) }
    )
    EventEffect(
        event = uiState.closeScreenWithSuccess,
        onConsumed = { viewModel.onScreenClosed() },
        action = { resultSender.navigateBack(result = true) }
    )
}

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalCoroutinesApi::class
)
@Composable
private fun PhotoConfirmationScreen(
    isLoading: Boolean,
    isError: Boolean,
    photo: Bitmap?,
    isUploadingPhoto: Boolean,
    uploadingMessage: String?,
    sheetState: ModalBottomSheetState,
    internetPopupEnabled: Boolean,
    actions: PhotoConfirmationActions
) {
    BottomSheetScaffold(
        state = sheetState,
        sheetContent = {
            ConfirmationBottomSheetContent(
                onDecline = actions.onPhotoConfirmationDeclined,
                onAccept = actions.onPhotoConfirmationAccepted
            )
        }
    ) {
        Surface(
            color = md_theme_dark_background,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (internetPopupEnabled) {
                    InternetConnectionPopup()
                }
                if (photo != null) {
                    PhotoPreviewContent(
                        modifier = Modifier.fillMaxSize(),
                        photo = photo,
                        onAccept = actions.onPhotoAccepted,
                        onCancel = actions.onPhotoDeclined
                    )
                }
                when {
                    isLoading -> Loader()
                    isUploadingPhoto -> LoaderDialog(label = uploadingMessage)
                    isError -> ErrorDialog { actions.onErrorDismissed() }
                }
            }
        }
    }
}

@Composable
private fun PhotoPreviewContent(
    modifier: Modifier = Modifier,
    photo: Bitmap,
    onAccept: () -> Unit,
    onCancel: () -> Unit
) {
    val bottomPadding = WindowInsets.navigationBars
        .only(WindowInsetsSides.Bottom)
        .asPaddingValues()
        .calculateBottomPadding()

    Column(
        modifier = modifier.padding(bottom = bottomPadding + Dimension.marginLarge)
    ) {
        Actions(
            onAccept = onAccept,
            onCancel = onCancel
        )
        Image(
            bitmap = photo.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier.weight(1.0f)
        )
    }
}

@Composable
private fun Actions(
    onAccept: () -> Unit,
    onCancel: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(Dimension.marginSmall)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        PlainButton(
            text = Label.cancel,
            color = md_theme_dark_onBackground,
            onClick = onCancel
        )
        PlainButton(
            text = Label.galleryPublishPhotoLabel,
            color = md_theme_dark_onBackground,
            onClick = onAccept
        )
    }
}

@Composable
private fun ConfirmationBottomSheetContent(
    onDecline: () -> Unit,
    onAccept: () -> Unit
) {
    Text(
        text = Label.galleryPublishConfirmationTitleText,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
    VerticalMargin(Dimension.marginNormal)
    Text(
        text = Label.galleryPublishConfirmationDescriptionText,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
    VerticalMargin(Dimension.marginLarge)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        PlainButton(
            text = Label.cancel,
            color = MaterialTheme.colorScheme.primary,
            onClick = onDecline
        )
        PlainButton(
            text = Label.galleryPublishPhotoLabel,
            color = MaterialTheme.colorScheme.primary,
            onClick = onAccept
        )
    }
}

private data class PhotoConfirmationActions(
    val onPhotoAccepted: () -> Unit,
    val onPhotoDeclined: () -> Unit,
    val onPhotoConfirmationAccepted: () -> Unit,
    val onPhotoConfirmationDeclined: () -> Unit,
    val onErrorDismissed: () -> Unit
)

@Suppress("MagicNumber")
@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
private fun PhotoConfirmationScreenPreview() {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Expanded)

    AppTheme(darkTheme = false) {
        PhotoConfirmationScreen(
            isLoading = false,
            isError = false,
            photo = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888),
            isUploadingPhoto = false,
            uploadingMessage = null,
            sheetState = sheetState,
            internetPopupEnabled = false,
            actions = PhotoConfirmationActions(
                onPhotoAccepted = {},
                onPhotoDeclined = {},
                onPhotoConfirmationAccepted = {},
                onPhotoConfirmationDeclined = {},
                onErrorDismissed = {}
            )
        )
    }
}
