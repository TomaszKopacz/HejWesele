package com.hejwesele.galleries

import com.hejwesele.galleries.model.Gallery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GalleriesRepository @Inject constructor(
    private val remoteSource: GalleriesRemoteSource,
    private val remoteStorage: GalleriesRemoteStorage
) {

    suspend fun observeGallery(galleryId: String) = withContext(Dispatchers.IO) {
        remoteSource.observeGallery(galleryId)
    }

    suspend fun getGallery(galleryId: String) = withContext(Dispatchers.IO) {
        remoteSource.getGallery(galleryId)
    }

    suspend fun addGallery(gallery: Gallery) = withContext(Dispatchers.IO) {
        remoteSource.addGallery(gallery)
    }

    suspend fun updateGallery(galleryId: String, gallery: Gallery) = withContext(Dispatchers.IO) {
        remoteSource.updateGallery(galleryId, gallery)
    }

    suspend fun uploadImage(path: String, bytes: ByteArray) = withContext(Dispatchers.IO) {
        remoteStorage.uploadImage(path, bytes)
    }
}
