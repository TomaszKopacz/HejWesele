package com.hejwesele.gallery.preview

import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.android.components.Loader
import com.hejwesele.android.components.LoaderDialog
import com.hejwesele.android.components.ScreenOrientationLocker
import com.hejwesele.android.theme.Dimension
import com.hejwesele.android.theme.Label
import com.hejwesele.android.theme.md_theme_dark_background
import com.hejwesele.android.theme.md_theme_dark_onBackground
import com.hejwesele.gallery.IGalleryNavigation
import com.hejwesele.gallery.R
import com.ramcosta.composedestinations.annotation.Destination
import de.palm.composestateevents.EventEffect

@Composable
@Destination(navArgsDelegate = GalleryPreviewNavArgs::class)
fun GalleryPreview(
    navigation: IGalleryNavigation,
) {
    GalleryPreviewScreen(navigation)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GalleryPreviewScreen(
    navigation: IGalleryNavigation,
    viewModel: GalleryPreviewViewModel = hiltViewModel()
) {
    ScreenOrientationLocker(SCREEN_ORIENTATION_PORTRAIT)
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = true
        )
    }

    val uiState by viewModel.states.collectAsState()
    val snackbarState = remember { SnackbarHostState() }

    val permissionsLauncher = rememberLauncherForActivityResult(RequestMultiplePermissions()) { permissionsResult ->
        viewModel.onStoragePermissionsResult(permissionsResult)
    }

    EventEffect(
        event = uiState.closeScreen,
        onConsumed = { viewModel.onScreenClosed() },
        action = { navigation.navigateUp() }
    )
    EventEffect(
        event = uiState.requestStoragePermissions,
        onConsumed = { viewModel.onStoragePermissionsRequested() },
        action = { permissions -> permissionsLauncher.launch(permissions) }
    )
    EventEffect(
        event = uiState.showSavePhotoSuccess,
        onConsumed = { viewModel.onSavePhotoResultShown() },
        action = {
            snackbarState.showSnackbar(
                message = Label.gallerySavingPhotoSuccessText,
                withDismissAction = true
            )
        }
    )
    EventEffect(
        event = uiState.showSavePhotoError,
        onConsumed = { viewModel.onSavePhotoResultShown() },
        action = {
            snackbarState.showSnackbar(
                message = Label.gallerySavingPhotoFailureText,
                withDismissAction = true
            )
        }
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarState) }
    ) { padding ->
        Surface(color = md_theme_dark_background) {
            with(uiState) {
                if (loading) {
                    Loader()
                } else {
                    GalleryPreviewContent(
                        padding = padding,
                        photoUrls = photoUrls,
                        selectedPhotoIndex = selectedPhotoIndex,
                        savingPhoto = savingPhoto,
                        onBack = { viewModel.onBack() },
                        onSave = { photoUrl -> viewModel.onSavePhotoClicked(photoUrl) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun GalleryPreviewContent(
    padding: PaddingValues,
    photoUrls: List<String>,
    selectedPhotoIndex: Int,
    savingPhoto: Boolean,
    onBack: () -> Unit,
    onSave: (String) -> Unit
) {
    val pagerState = rememberPagerState(initialPage = selectedPhotoIndex)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding()
            ),
    ) {
        Actions(
            onBack = onBack,
            onSave = { onSave(photoUrls[pagerState.currentPage]) }
        )
        PhotosCarousel(
            modifier = Modifier
                .weight(1.0f)
                .padding(bottom = padding.calculateBottomPadding()),
            state = pagerState,
            photoUrls = photoUrls,
            savingPhoto = savingPhoto
        )
    }
}

@Composable
private fun Actions(
    onBack: () -> Unit,
    onSave: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(Dimension.marginSmall)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_left),
            contentDescription = null,
            tint = md_theme_dark_onBackground,
            modifier = Modifier
                .size(Dimension.iconSizeNormal)
                .clickable { onBack() },
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_download),
            contentDescription = null,
            tint = md_theme_dark_onBackground,
            modifier = Modifier
                .size(Dimension.iconSizeNormal)
                .clickable { onSave() },
        )
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalGlideComposeApi::class)
@Composable
private fun PhotosCarousel(
    modifier: Modifier,
    state: PagerState,
    photoUrls: List<String>,
    savingPhoto: Boolean
) {
    HorizontalPager(
        modifier = modifier,
        state = state,
        count = photoUrls.count(),
    ) { page ->
        GlideImage(
            model = photoUrls[page],
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )
        if (savingPhoto) {
            LoaderDialog(label = Label.gallerySavingPhotoInProgressText)
        }
    }
}
