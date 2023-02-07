package com.hejwesele.gallery.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.hejwesele.gallery.Gallery

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.galleryGraph(route: String) {
    navigation(startDestination = GalleryRoutes.main, route = route) {
        composable(GalleryRoutes.main) {
            Gallery(
                viewModel = hiltViewModel()
            )
        }
    }
}
