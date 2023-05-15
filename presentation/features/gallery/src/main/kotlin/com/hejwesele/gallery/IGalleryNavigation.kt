package com.hejwesele.gallery

import android.net.Uri

interface IGalleryNavigation {
    fun openPhotoConfirmation(photoUri: Uri, galleryId: String)
    fun openPreview(photoUrls: ArrayList<String>, selectedPhotoIndex: Int)

    fun logout()

    fun navigateUp()
}
