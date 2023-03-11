package com.hejwesele.gallery.confirmation

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateViewModel
import com.hejwesele.extensions.BitmapResolver
import com.hejwesele.gallery.confirmation.usecase.AddPhotoToGallery
import com.hejwesele.gallery.destinations.PhotoConfirmationDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class PhotoConfirmationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val bitmapResolver: BitmapResolver,
    private val addPhotoToGallery: AddPhotoToGallery
) : StateViewModel<PhotoConfirmationUiState>(PhotoConfirmationUiState.DEFAULT) {

    private var state = InternalState()

    init {
        viewModelScope.launch {
            updateState { copy(loading = true) }

            val uri = PhotoConfirmationDestination.argsFrom(savedStateHandle).photoUri
            val galleryId = PhotoConfirmationDestination.argsFrom(savedStateHandle).galleryId
            val photo = bitmapResolver.getBitmap(uri)

            state = state.copy(photo = photo, galleryId = galleryId)
            updateState { copy(loading = false, photo = photo) }
        }
    }

    fun onPhotoDeclined() {
        updateState { copy(closeScreen = triggered) }
    }

    fun onPhotoAccepted() {
        viewModelScope.launch {
            updateState { copy(showPhotoConfirmation = triggered) }
        }
    }

    fun onPhotoConfirmationDeclined() {
        viewModelScope.launch {
            updateState { copy(hidePhotoConfirmation = triggered) }
        }
    }

    fun onPhotoConfirmationAccepted() {
        val photo = requireNotNull(state.photo)
        val galleryId = requireNotNull(state.galleryId)

        viewModelScope.launch {
            updateState { copy(loading = true, hidePhotoConfirmation = triggered) }
            withContext(Dispatchers.IO) {
                addPhotoToGallery(
                    galleryId = galleryId,
                    photo = photo
                ).onSuccess {
                    updateState { copy(loading = false, closeScreen = triggered) }
                }.onFailure {
                    // TODO - show error
                }
            }
        }
    }

    fun onPhotoConfirmationShown() {
        updateState { copy(showPhotoConfirmation = consumed) }
    }

    fun onPhotoConfirmationHidden() {
        updateState { copy(hidePhotoConfirmation = consumed) }
    }

    fun onScreenClosed() {
        viewModelScope.launch {
            updateState { copy(closeScreen = consumed) }
        }
    }

    private data class InternalState(
        val photo: Bitmap? = null,
        val galleryId: String? = null
    )
}

internal data class PhotoConfirmationUiState(
    val showPhotoConfirmation: StateEvent,
    val hidePhotoConfirmation: StateEvent,
    val closeScreen: StateEvent,
    val loading: Boolean,
    val photo: Bitmap?,
    val error: Throwable?
) {
    companion object {
        val DEFAULT = PhotoConfirmationUiState(
            showPhotoConfirmation = consumed,
            hidePhotoConfirmation = consumed,
            closeScreen = consumed,
            loading = false,
            photo = null,
            error = null
        )
    }
}
