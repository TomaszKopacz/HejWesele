package com.hejwesele.gallery.board.usecase

import com.hejwesele.galleries.GalleriesRepository
import com.hejwesele.galleries.model.Gallery
import com.hejwesele.result.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveGallery @Inject constructor(
    private val repository: GalleriesRepository
) {

    operator fun invoke(galleryId: String): Flow<Result<Gallery>> =
        repository.observeGallery(galleryId)
}
