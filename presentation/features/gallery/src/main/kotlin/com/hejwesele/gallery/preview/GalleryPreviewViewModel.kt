package com.hejwesele.gallery.preview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateViewModel
import com.hejwesele.gallery.destinations.GalleryPreviewDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class GalleryPreviewViewModel @Inject constructor(
    state: SavedStateHandle
) : StateViewModel<GalleryPreviewUiState>(GalleryPreviewUiState.DEFAULT) {

    init {
        viewModelScope.launch {
            updateState { copy(loading = true) }

            val photos = GalleryPreviewDestination.argsFrom(state).photos
            val selectedPhoto = GalleryPreviewDestination.argsFrom(state).selectedPhoto

            updateState {
                copy(
                    loading = false,
                    photos = photos,
                    selectedPhoto = selectedPhoto
                )
            }
        }
    }
}

internal data class GalleryPreviewUiState(
    val loading: Boolean,
    val photos: ArrayList<String>,
    val selectedPhoto: Int
) {
    companion object {
        val DEFAULT = GalleryPreviewUiState(
            loading = false,
            photos = arrayListOf(),
            selectedPhoto = 0
        )
    }

}
