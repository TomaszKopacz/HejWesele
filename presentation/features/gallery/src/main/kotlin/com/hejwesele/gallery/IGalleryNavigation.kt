package com.hejwesele.gallery

import android.net.Uri

interface IGalleryNavigation {
    fun openPhotoConfirmation(photoUri: Uri, galleryId: String)
    fun openPreview(photo: String)

    fun openSettings()
}
