package com.hejwesele.gallery.board.usecase

import com.hejwesele.galleries.GalleriesRepository
import javax.inject.Inject

class ObserveGallery @Inject constructor(
    private val repository: GalleriesRepository
) {

    operator fun invoke(galleryId: String) = repository.observeGallery(galleryId)
}
