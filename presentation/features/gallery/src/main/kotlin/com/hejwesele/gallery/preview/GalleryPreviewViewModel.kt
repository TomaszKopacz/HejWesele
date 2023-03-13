package com.hejwesele.gallery.preview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateViewModel
import com.hejwesele.gallery.destinations.GalleryPreviewDestination
import com.hejwesele.gallery.preview.usecase.SaveImage
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class GalleryPreviewViewModel @Inject constructor(
    state: SavedStateHandle,
    private val savePhoto: SaveImage
) : StateViewModel<GalleryPreviewUiState>(GalleryPreviewUiState.DEFAULT) {

    init {
        viewModelScope.launch {
            updateState { copy(loading = true) }

            val photoUrls = GalleryPreviewDestination.argsFrom(state).photoUrls
            val selectedPhotoIndex = GalleryPreviewDestination.argsFrom(state).selectedPhotoIndex

            updateState {
                copy(
                    loading = false,
                    photoUrls = photoUrls,
                    selectedPhotoIndex = selectedPhotoIndex
                )
            }
        }
    }

    fun onBack() {
        viewModelScope.launch {
            updateState { copy(closeScreen = triggered) }
        }
    }

    fun onSavePhotoClicked(photoUrl: String) {
        updateState { copy(savingPhoto = true) }
        viewModelScope.launch(Dispatchers.IO) {
            savePhoto(photoUrl)
                .onSuccess {
                    updateState {
                        copy(
                            showSavePhotoSuccess = triggered,
                            savingPhoto = false
                        )
                    }
                }
                .onFailure {
                    updateState {
                        copy(
                            showSavePhotoError = triggered,
                            savingPhoto = false
                        )
                    }
                }
        }
    }

    fun onSavePhotoResultShown() {
        viewModelScope.launch {
            updateState {
                copy(
                    showSavePhotoSuccess = consumed,
                    showSavePhotoError = consumed
                )
            }
        }
    }

    fun onScreenClosed() {
        viewModelScope.launch {
            updateState { copy(closeScreen = consumed) }
        }
    }
}

internal data class GalleryPreviewUiState(
    val closeScreen: StateEvent,
    val showSavePhotoSuccess: StateEvent,
    val showSavePhotoError: StateEvent,
    val photoUrls: ArrayList<String>,
    val selectedPhotoIndex: Int,
    val loading: Boolean,
    val savingPhoto: Boolean
) {
    companion object {
        val DEFAULT = GalleryPreviewUiState(
            closeScreen = consumed,
            showSavePhotoSuccess = consumed,
            showSavePhotoError = consumed,
            photoUrls = arrayListOf(),
            selectedPhotoIndex = 0,
            loading = false,
            savingPhoto = false
        )
    }
}
