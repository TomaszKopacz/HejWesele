package com.hejwesele.galleries

import com.hejwesele.galleries.dto.GalleryDto
import com.hejwesele.galleries.mappers.mapDto
import com.hejwesele.galleries.mappers.mapModel
import com.hejwesele.galleries.model.Gallery
import com.hejwesele.realtimedatabase.FirebaseRealtimeDatabase
import com.hejwesele.result.notFound
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FirebaseGalleriesRemoteSource @Inject constructor(
    private val database: FirebaseRealtimeDatabase
) : GalleriesRemoteSource {

    companion object {
        private const val GALLERIES_PATH = "galleries/"
    }

    override fun observeGallery(galleryId: String): Flow<Result<Gallery>> {
        return database.observe(
            path = GALLERIES_PATH,
            id = galleryId,
            type = GalleryDto::class
        ).map { result ->
            result.mapCatching { dto -> dto.mapModel() }
        }
    }

    override suspend fun getGallery(galleryId: String): Result<Gallery> {
        return database.read(
            path = GALLERIES_PATH,
            id = galleryId,
            type = GalleryDto::class
        ).mapCatching { dto ->
            dto?.mapModel() ?: throw notFound(name = "gallery", id = galleryId)
        }
    }

    override suspend fun addGallery(gallery: Gallery): Result<Gallery> {
        return database.write(
            path = GALLERIES_PATH,
            item = gallery.mapDto()
        ).map { gallery }
    }
}
