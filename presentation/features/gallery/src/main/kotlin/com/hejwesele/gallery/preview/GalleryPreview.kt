package com.hejwesele.gallery.preview

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.android.components.Loader
import com.hejwesele.android.components.LoaderDialog
import com.hejwesele.android.components.RectangleImage
import com.hejwesele.android.components.ScreenOrientationLocker
import com.hejwesele.android.theme.AppTheme
import com.hejwesele.android.theme.Dimension
import com.hejwesele.android.theme.Label
import com.hejwesele.android.theme.md_theme_dark_background
import com.hejwesele.android.theme.md_theme_dark_onBackground
import com.hejwesele.gallery.IGalleryNavigation
import com.hejwesele.internet.InternetConnectionPopup
import com.hejwesele.theme.R
import com.ramcosta.composedestinations.annotation.Destination
import de.palm.composestateevents.EventEffect
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
@Destination(navArgsDelegate = GalleryPreviewNavArgs::class)
fun GalleryPreview(navigation: IGalleryNavigation) {
    GalleryPreviewEntryPoint(navigation)
}

@Composable
private fun GalleryPreviewEntryPoint(
    navigation: IGalleryNavigation,
    viewModel: GalleryPreviewViewModel = hiltViewModel()
) {
    ScreenOrientationLocker(SCREEN_ORIENTATION_PORTRAIT)
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = Color.Transparent, darkIcons = true)
    }

    val uiState by viewModel.states.collectAsState()
    val uiEvents by viewModel.events.collectAsState()
    val snackbarState = remember { SnackbarHostState() }

    val permissionsLauncher = rememberLauncherForActivityResult(RequestMultiplePermissions()) { permissionsResult ->
        viewModel.onStoragePermissionsResult(permissionsResult)
    }

    GalleryPreviewEventsHandler(
        events = uiEvents,
        viewModel = viewModel,
        navigation = navigation,
        snackbarState = snackbarState,
        permissionsLauncher = permissionsLauncher
    )

    val data = with(uiState) {
        GalleryPreviewData(
            isLoading = isLoading,
            photoUrls = photoUrls,
            selectedPhotoIndex = selectedPhotoIndex,
            isSavingPhoto = isSavingPhoto,
            isInternetPopupEnabled = true
        )
    }

    val actions = with(viewModel) {
        GalleryPreviewActions(
            onBackClicked = ::onBack,
            onSaveClicked = ::onSavePhotoClicked
        )
    }

    GalleryPreviewScreen(
        data = data,
        actions = actions,
        snackbarState = snackbarState
    )
}

@Composable
private fun GalleryPreviewEventsHandler(
    events: GalleryPreviewUiEvents,
    viewModel: GalleryPreviewViewModel,
    navigation: IGalleryNavigation,
    permissionsLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
    snackbarState: SnackbarHostState
) {
    EventEffect(
        event = events.closeScreen,
        onConsumed = viewModel::onScreenClosed,
        action = navigation::navigateUp
    )
    EventEffect(
        event = events.requestStoragePermissions,
        onConsumed = viewModel::onStoragePermissionsRequested,
        action = permissionsLauncher::launch
    )
    EventEffect(
        event = events.showSavePhotoSuccess,
        onConsumed = viewModel::onSavePhotoResultShown,
        action = {
            snackbarState.showSnackbar(
                message = Label.gallerySavingPhotoSuccessText,
                withDismissAction = true
            )
        }
    )
    EventEffect(
        event = events.showSavePhotoError,
        onConsumed = viewModel::onSavePhotoResultShown,
        action = {
            snackbarState.showSnackbar(
                message = Label.gallerySavingPhotoFailureText,
                withDismissAction = true
            )
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class,
    ExperimentalCoroutinesApi::class
)
@Composable
private fun GalleryPreviewScreen(
    data: GalleryPreviewData,
    actions: GalleryPreviewActions,
    snackbarState: SnackbarHostState
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarState) }
    ) {
        Surface(color = md_theme_dark_background) {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (data.isInternetPopupEnabled) {
                    InternetConnectionPopup()
                }
                GalleryPreviewContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding(),
                    photoUrls = data.photoUrls,
                    selectedPhotoIndex = data.selectedPhotoIndex,
                    isSavingPhoto = data.isSavingPhoto,
                    onBack = actions.onBackClicked,
                    onSave = { photoUrl -> actions.onSaveClicked(photoUrl) }
                )
                if (data.isLoading) {
                    Loader()
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun GalleryPreviewContent(
    modifier: Modifier = Modifier,
    photoUrls: List<String>,
    selectedPhotoIndex: Int,
    isSavingPhoto: Boolean,
    onBack: () -> Unit,
    onSave: (String) -> Unit
) {
    val pagerState = rememberPagerState(initialPage = selectedPhotoIndex)
    Column(modifier = modifier) {
        Actions(
            modifier = Modifier
                .padding(horizontal = Dimension.marginSmall)
                .fillMaxWidth(),
            onBack = onBack,
            onSave = { onSave(photoUrls[pagerState.currentPage]) }
        )
        PhotosCarousel(
            modifier = Modifier.weight(1.0f),
            state = pagerState,
            photoUrls = photoUrls,
            savingPhoto = isSavingPhoto
        )
    }
}

@Composable
private fun Actions(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_left),
            contentDescription = null,
            tint = md_theme_dark_onBackground,
            modifier = Modifier
                .size(Dimension.iconNormal)
                .clickable { onBack() }
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_download),
            contentDescription = null,
            tint = md_theme_dark_onBackground,
            modifier = Modifier
                .size(Dimension.iconNormal)
                .clickable { onSave() }
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun PhotosCarousel(
    modifier: Modifier = Modifier,
    state: PagerState,
    photoUrls: List<String>,
    savingPhoto: Boolean
) {
    HorizontalPager(
        modifier = modifier,
        state = state,
        count = photoUrls.count()
    ) { page ->
        val url = photoUrls[page]

        RectangleImage(
            modifier = Modifier.fillMaxWidth(),
            url = url
        )
        if (savingPhoto) {
            LoaderDialog(label = Label.gallerySavingPhotoInProgressText)
        }
    }
}

private data class GalleryPreviewData(
    val isLoading: Boolean,
    val photoUrls: List<String>,
    val selectedPhotoIndex: Int,
    val isSavingPhoto: Boolean,
    val isInternetPopupEnabled: Boolean
) {
    companion object {
        val Preview = GalleryPreviewData(
            isLoading = false,
            photoUrls = listOf("fake url 1", "fake url 2"),
            selectedPhotoIndex = 1,
            isSavingPhoto = false,
            isInternetPopupEnabled = false
        )
    }
}

private data class GalleryPreviewActions(
    val onBackClicked: () -> Unit,
    val onSaveClicked: (String) -> Unit
) {
    companion object {
        val Preview = GalleryPreviewActions(
            onBackClicked = {},
            onSaveClicked = {}
        )
    }
}

@Preview
@Composable
private fun GalleryPreviewScreenPreview() {
    val snackbarState = remember { SnackbarHostState() }

    AppTheme(darkTheme = false) {
        GalleryPreviewScreen(
            data = GalleryPreviewData.Preview,
            actions = GalleryPreviewActions.Preview,
            snackbarState = snackbarState
        )
    }
}
