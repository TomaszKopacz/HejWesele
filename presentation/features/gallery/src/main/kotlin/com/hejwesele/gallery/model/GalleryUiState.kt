package com.hejwesele.gallery.model

import com.hejwesele.result.GeneralError

data class GalleryUiState(
    val action: GalleryUiAction?,
    val loading: Boolean,
    val galleryHintVisible: Boolean,
    val galleryLinkVisible: Boolean,
    val photos: List<String>,
    val error: GeneralError?,
) {
    companion object {
        val DEFAULT = GalleryUiState(
            action = null,
            loading = false,
            galleryHintVisible = true,
            galleryLinkVisible = false,
            photos = emptyList(),
            error = null
        )
    }
}
