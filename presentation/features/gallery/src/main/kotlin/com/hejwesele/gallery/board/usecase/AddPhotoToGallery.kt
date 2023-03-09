package com.hejwesele.gallery.board.usecase

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.PNG
import com.hejwesele.galleries.GalleriesRepository
import com.hejwesele.result.extensions.flatMap
import javax.inject.Inject

class AddPhotoToGallery @Inject constructor(
    private val uploadImage: UploadImage,
    private val repository: GalleriesRepository
) {

    suspend operator fun invoke(
        galleryId: String,
        photo: Bitmap
    ): Result<String> {
        return uploadImage(
            galleryId = galleryId,
            bitmap = photo,
            format = PNG
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
}
