package com.hejwesele.galleries

import com.hejwesele.galleries.dto.GalleryDto
import com.hejwesele.galleries.mappers.mapDto
import com.hejwesele.galleries.mappers.safeMapModel
import com.hejwesele.galleries.model.Gallery
import com.hejwesele.realtimedatabase.FirebaseRealtimeDatabase
import com.hejwesele.realtimedatabase.FirebaseResult.Error
import com.hejwesele.realtimedatabase.FirebaseResult.NoSuchItem
import com.hejwesele.realtimedatabase.FirebaseResult.Success
import com.hejwesele.result.CompletableResult
import com.hejwesele.result.Result
import com.hejwesele.result.completed
import com.hejwesele.result.failed
import com.hejwesele.result.failure
import com.hejwesele.result.flatMapSuccess
import com.hejwesele.result.serviceError
import com.hejwesele.result.success
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
        ).map { firebaseResult ->
            when (firebaseResult) {
                is Success -> success(firebaseResult.value)
                is Error -> failure(serviceError(firebaseResult.exception))
                is NoSuchItem -> failure(serviceError())
            }
        }.map { result ->
            result.flatMapSuccess {
                it.safeMapModel()
            }
        }
    }

    override suspend fun getGallery(galleryId: String): Result<Gallery> {
        val result = database.read(
            path = GALLERIES_PATH,
            id = galleryId,
            type = GalleryDto::class
        )

        return when (result) {
            is Success -> success(result.value)
            is Error -> failure(serviceError(result.exception))
            is NoSuchItem -> failure(serviceError())
        }.flatMapSuccess {
            it.safeMapModel()
        }
    }

    override suspend fun addGallery(gallery: Gallery): CompletableResult {
        val tileSaved = database.write(
            path = GALLERIES_PATH,
            item = gallery.mapDto()
        )

        return if (tileSaved) completed() else failed(serviceError())
    }
}
