package com.hejwesele.gallery.model

import com.hejwesele.result.GeneralError

data class GalleryUiState(
    val loading: Boolean,
    val galleryHintVisible: Boolean,
    val galleryLinkVisible: Boolean,
    val photos: List<String>,
    val error: GeneralError?
) {
    companion object {
        val DEFAULT = GalleryUiState(
            loading = false,
            galleryHintVisible = true,
            galleryLinkVisible = false,
            photos = emptyList(),
            error = null
        )
    }
}
