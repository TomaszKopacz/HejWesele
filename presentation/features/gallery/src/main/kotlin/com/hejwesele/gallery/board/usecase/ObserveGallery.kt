package com.hejwesele.gallery.board.usecase

import com.hejwesele.galleries.GalleriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ObserveGallery @Inject constructor(
    private val repository: GalleriesRepository
) {

    suspend operator fun invoke(galleryId: String) = withContext(Dispatchers.IO) {
        repository.observeGallery(galleryId)
    }
}
