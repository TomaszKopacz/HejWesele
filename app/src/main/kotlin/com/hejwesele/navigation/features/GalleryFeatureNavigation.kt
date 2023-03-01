package com.hejwesele.navigation.features

import androidx.navigation.NavController
import com.hejwesele.gallery.IGalleryNavigation
import com.hejwesele.gallery.preview.destinations.GalleryPreviewDestination
import com.hejwesele.navigation.CommonNavigation
import com.hejwesele.navigation.ICommonNavigation
import com.hejwesele.navigation.MainNavGraph
import com.hejwesele.navigation.SettingsNavGraph
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.navigate

class GalleryFeatureNavigation(
    private val navController: NavController
) : IGalleryNavigation, ICommonNavigation by CommonNavigation(navController) {

    override fun openPreview(photo: String) {
        navController.navigate(GalleryPreviewDestination(photo = photo) within MainNavGraph)
    }

    override fun openSettings() {
        navController.navigate(SettingsNavGraph)
    }
}
