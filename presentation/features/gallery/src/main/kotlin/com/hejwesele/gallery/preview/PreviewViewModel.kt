package com.hejwesele.gallery.preview

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.hejwesele.android.mvvm.StateActionsViewModel
import com.hejwesele.gallery.model.GalleryUiAction
import com.hejwesele.gallery.model.GalleryUiState
import com.hejwesele.gallery.preview.destinations.PreviewDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class PreviewViewModel @Inject constructor(
    state: SavedStateHandle
) : StateActionsViewModel<GalleryUiState, GalleryUiAction>(GalleryUiState.DEFAULT) {


    private val photo: String = PreviewDestination.argsFrom(state).photo

    init {
        Log.d("TOMASZKOPACZ", "Photo: $photo")
    }
}
