package com.hejwesele.gallery.preview

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.hejwesele.gallery.preview.destinations.GalleryPreviewDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class GalleryPreviewViewModel @Inject constructor(
    state: SavedStateHandle
) : ViewModel() {

    private val photo: String = GalleryPreviewDestination.argsFrom(state).photo

    init {
        Log.d("TOMASZKOPACZ", "Photo: $photo")
    }
}
