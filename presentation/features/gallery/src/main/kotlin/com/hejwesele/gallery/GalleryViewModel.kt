package com.hejwesele.gallery

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateActionsViewModel
import com.hejwesele.gallery.model.GalleryUiAction
import com.hejwesele.gallery.model.GalleryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class GalleryViewModel @Inject constructor()
    : StateActionsViewModel<GalleryUiState, GalleryUiAction>(GalleryUiState.DEFAULT) {

    init {
        viewModelScope.launch {

        }
    }
}
