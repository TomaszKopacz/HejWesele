package com.hejwesele.galleries

import com.hejwesele.galleries.model.Gallery
import kotlinx.coroutines.flow.Flow

interface GalleriesRemoteSource {

    suspend fun observeGallery(galleryId: String): Flow<Result<Gallery>>

    suspend fun getGallery(galleryId: String): Result<Gallery>

    suspend fun addGallery(gallery: Gallery): Result<Gallery>

    suspend fun updateGallery(galleryId: String, gallery: Gallery): Result<Gallery>
}
