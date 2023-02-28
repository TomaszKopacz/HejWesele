package com.hejwesele.navigation

import androidx.navigation.NavController
import com.hejwesele.gallery.navigation.GalleryFeatureNavigation
import com.hejwesele.gallery.preview.destinations.PreviewDestination
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.navigate

class GalleryFeatureNavigationImpl(
    private val navController: NavController
) : GalleryFeatureNavigation, CommonNavigation by CommonNavigatorImpl(navController) {

    override fun openPreview(photo: String) {
        navController.navigate(PreviewDestination(photo = photo) within MainNavGraph)
    }
}
