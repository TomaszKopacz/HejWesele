package com.hejwesele.gallery.preview

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.android.components.Loader
import com.hejwesele.android.components.PlainButton
import com.hejwesele.android.theme.Dimension
import com.hejwesele.android.theme.Label
import com.hejwesele.android.theme.md_theme_dark_background
import com.hejwesele.android.theme.md_theme_dark_onBackground
import com.hejwesele.gallery.IGalleryNavigation
import com.ramcosta.composedestinations.annotation.Destination

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
    LockScreenOrientation(SCREEN_ORIENTATION_PORTRAIT)
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = true
        )
    }

    val coroutineScope = rememberCoroutineScope()

    val uiState by viewModel.states.collectAsState()
    val snackbarState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarState) }
    ) { padding ->
        Surface(color = md_theme_dark_background) {
            with(uiState) {
                if (loading) {
                    Loader()
                } else {
                    PhotoPreviewContent(
                        padding = padding,
                        photos = uiState.photos,
                        selectedPhoto = uiState.selectedPhoto
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalGlideComposeApi::class)
@Composable
private fun PhotoPreviewContent(
    padding: PaddingValues,
    photos: List<String>,
    selectedPhoto: Int
) {
    val pagerState = rememberPagerState(initialPage = selectedPhoto)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding()
            ),
    ) {
        Actions(
            onBack = { },
            onSave = {
                Log.d("TOMASZKOPACZ", "Save item nr: ${pagerState.currentPage}")
            }
        )
        HorizontalPager(
            modifier = Modifier
                .weight(1.0f)
                .padding(bottom = padding.calculateBottomPadding()),
            state = pagerState,
            count = photos.count(),
        ) { page ->
            GlideImage(
                model = photos[page],
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )
        }
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
        PlainButton(
            text = Label.cancel,
            color = md_theme_dark_onBackground,
            onClick = onBack
        )
        PlainButton(
            text = Label.galleryPublishPhoto,
            color = md_theme_dark_onBackground,
            onClick = onSave
        )
    }
}

@Composable
fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(orientation) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            activity.requestedOrientation = originalOrientation
        }
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
