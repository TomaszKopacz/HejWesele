package com.hejwesele.gallery.confirmation.usecase

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.JPEG
import com.hejwesele.galleries.GalleriesRepository
import com.hejwesele.result.extensions.flatMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddPhotoToGallery @Inject constructor(
    private val uploadImage: UploadImage,
    private val repository: GalleriesRepository
) {

    suspend operator fun invoke(
        galleryId: String,
        photo: Bitmap
    ): Result<String> = withContext(Dispatchers.IO) {
        uploadImage(
            location = GALLERY_LOCATION,
            folder = galleryId,
            image = photo,
            format = JPEG
        ).flatMap { photoUrl ->
            addPhotoUrlToGallery(galleryId, photoUrl)
        }
    }

    private suspend fun addPhotoUrlToGallery(galleryId: String, photoUrl: String): Result<String> {
        return repository.getGallery(galleryId)
            .flatMap { gallery ->
                val photos = gallery.photos.toMutableList().also {
                    it.add(photoUrl)
                }

                repository.updateGallery(
                    galleryId = galleryId,
                    gallery = gallery.copy(
                        photos = photos.toList()
                    )
                ).map { photoUrl }
            }
    }

    companion object {
        private const val GALLERY_LOCATION = "gallery"
    }
}
