package com.hejwesele.gallery.confirmation

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.android.components.ErrorDialog
import com.hejwesele.android.components.Loader
import com.hejwesele.android.components.LoaderDialog
import com.hejwesele.android.components.PlainButton
import com.hejwesele.android.components.layouts.BottomSheetScaffold
import com.hejwesele.android.theme.Dimension
import com.hejwesele.android.theme.Label
import com.hejwesele.android.theme.md_theme_dark_background
import com.hejwesele.android.theme.md_theme_dark_onBackground
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import de.palm.composestateevents.EventEffect
import kotlinx.coroutines.launch

@Composable
@Destination(navArgsDelegate = PhotoConfirmationNavArgs::class)
fun PhotoConfirmation(
    resultSender: ResultBackNavigator<Boolean>
) {
    PhotoConfirmationScreen(resultSender)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PhotoConfirmationScreen(
    resultSender: ResultBackNavigator<Boolean>,
    viewModel: PhotoConfirmationViewModel = hiltViewModel()
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
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )

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

    BottomSheetScaffold(
        state = sheetState,
        sheetContent = {
            ConfirmationBottomSheetContent(
                onDecline = { viewModel.onPhotoConfirmationDeclined() },
                onAccept = { viewModel.onPhotoConfirmationAccepted() }
            )
        }
    ) {
        Surface(
            color = md_theme_dark_background,
            modifier = Modifier.fillMaxSize()
        ) {
            with(uiState) {
                if (photo != null) {
                    PhotoPreviewContent(
                        photo = photo,
                        onAccept = { viewModel.onPhotoAccepted() },
                        onCancel = { viewModel.onPhotoDeclined() }
                    )
                }
                if (loadingData) {
                    Loader()
                }
                if (uploadingPhoto) {
                    LoaderDialog(label = uploadingMessage)
                }
                if (error != null) {
                    ErrorDialog(
                        onDismiss = { viewModel.onErrorDismissed() }
                    )
                }
            }
        }
    }

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }
}

@Composable
private fun PhotoPreviewContent(
    photo: Bitmap,
    onAccept: () -> Unit,
    onCancel: () -> Unit
) {
    val topPadding = WindowInsets.statusBars
        .only(WindowInsetsSides.Top)
        .asPaddingValues()
        .calculateTopPadding()

    val bottomPadding = WindowInsets.navigationBars
        .only(WindowInsetsSides.Bottom)
        .asPaddingValues()
        .calculateBottomPadding()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = topPadding,
                bottom = bottomPadding
            )
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
            text = Label.galleryPublishPhoto,
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
    val bottomPadding = WindowInsets.navigationBars
        .only(WindowInsetsSides.Bottom)
        .asPaddingValues()
        .calculateBottomPadding()

    Text(
        text = Label.galleryPublishConfirmationTitle,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(modifier = Modifier.height(Dimension.marginNormal))
    Text(
        text = Label.galleryPublishConfirmationDescription,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(modifier = Modifier.height(Dimension.marginLarge))
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
            text = Label.galleryPublishPhoto,
            color = MaterialTheme.colorScheme.primary,
            onClick = onAccept
        )
    }
    Spacer(modifier = Modifier.height(bottomPadding))
}
