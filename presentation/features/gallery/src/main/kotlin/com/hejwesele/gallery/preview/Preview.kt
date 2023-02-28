package com.hejwesele.gallery.preview

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.hejwesele.gallery.navigation.GalleryFeatureNavigation

import com.ramcosta.composedestinations.annotation.Destination

data class PreviewNavArgs(val photo: String)

@Composable
@Destination(navArgsDelegate = PreviewNavArgs::class)
fun Preview(
    navigation: GalleryFeatureNavigation
) {
    PreviewScreen()
}

@Composable
private fun PreviewScreen(
    viewModel: PreviewViewModel = hiltViewModel()
) {
    Text("Preview")
}
