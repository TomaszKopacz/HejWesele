package com.hejwesele.gallery.model

data class GalleryUiState(
    val photos: List<String> = emptyList()
) {
    companion object {
        val DEFAULT = GalleryUiState()
    }
}
