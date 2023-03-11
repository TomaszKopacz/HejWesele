package com.hejwesele.galleries

import com.hejwesele.galleries.model.Gallery
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GalleriesRepository @Inject constructor(
    private val remoteSource: GalleriesRemoteSource,
    private val remoteStorage: GalleriesRemoteStorage
) {

    fun observeGallery(galleryId: String) = remoteSource.observeGallery(galleryId)

    suspend fun getGallery(galleryId: String) = remoteSource.getGallery(galleryId)

    suspend fun addGallery(gallery: Gallery) = remoteSource.addGallery(gallery)

    suspend fun updateGallery(galleryId: String, gallery: Gallery) = remoteSource.updateGallery(galleryId, gallery)

    suspend fun uploadImage(path: String, bytes: ByteArray) = remoteStorage.uploadImage(path, bytes)
}
