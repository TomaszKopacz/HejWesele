package com.hejwesele.galleries

import com.hejwesele.galleries.model.Gallery
import javax.inject.Inject

class GalleriesRepository @Inject constructor(
    private val remoteSource: GalleriesRemoteSource
) {

    fun observeGallery(galleryId: String) = remoteSource.observeGallery(galleryId)

    suspend fun getGallery(galleryId: String) = remoteSource.getGallery(galleryId)

    suspend fun addGallery(gallery: Gallery) = remoteSource.addGallery(gallery)
}
