package com.hejwesele.gallery.preview

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.hejwesele.gallery.IGalleryNavigation
import com.ramcosta.composedestinations.annotation.Destination

@Suppress("UnusedPrivateMember")
@Composable
@Destination(navArgsDelegate = PreviewNavArgs::class)
fun GalleryPreview(
    navigation: IGalleryNavigation
) {
    GalleryPreviewScreen()
}

@Suppress("UnusedPrivateMember")
@Composable
private fun GalleryPreviewScreen(
    viewModel: GalleryPreviewViewModel = hiltViewModel()
) {
    Text("Preview")
}
