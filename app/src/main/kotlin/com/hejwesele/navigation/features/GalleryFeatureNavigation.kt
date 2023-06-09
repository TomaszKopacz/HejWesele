package com.hejwesele.navigation.features

import android.net.Uri
import androidx.navigation.NavController
import com.hejwesele.gallery.IGalleryNavigation
import com.hejwesele.gallery.destinations.GalleryPreviewDestination
import com.hejwesele.gallery.destinations.PhotoConfirmationDestination
import com.hejwesele.navigation.CommonNavigation
import com.hejwesele.navigation.ICommonNavigation
import com.hejwesele.navigation.LoginNavGraph
import com.hejwesele.navigation.MainNavGraph
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.navigate

class GalleryFeatureNavigation(
    private val navController: NavController
) : IGalleryNavigation, ICommonNavigation by CommonNavigation(navController) {

    override fun openPhotoConfirmation(photoUri: Uri, galleryId: String) {
        navController.navigate(
            PhotoConfirmationDestination(
                photoUri = photoUri,
                galleryId = galleryId
            ) within MainNavGraph
        )
    }

    override fun openPreview(photoUrls: ArrayList<String>, selectedPhotoIndex: Int) {
        navController.navigate(
            GalleryPreviewDestination(
                photoUrls = photoUrls,
                selectedPhotoIndex = selectedPhotoIndex
            ) within MainNavGraph
        )
    }

    override fun logout() {
        navController.navigate(LoginNavGraph) {
            popUpTo(MainNavGraph.route) { inclusive = true }
        }
    }
}
