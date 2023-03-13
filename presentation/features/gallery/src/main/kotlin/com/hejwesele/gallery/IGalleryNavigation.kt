package com.hejwesele.gallery

import android.net.Uri

interface IGalleryNavigation {
    fun openPhotoConfirmation(photoUri: Uri, galleryId: String)
    fun openPreview(photos: ArrayList<String>, selectedPhoto: Int)

    fun openSettings()
}
