package com.hejwesele.gallery.confirmation

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hejwesele.android.components.AlertData
import com.hejwesele.android.mvvm.StateEventsViewModel
import com.hejwesele.android.theme.Label
import com.hejwesele.bitmap.BitmapProvider
import com.hejwesele.gallery.confirmation.usecase.AddPhotoToGallery
import com.hejwesele.gallery.destinations.PhotoConfirmationDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PhotoConfirmationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val bitmapProvider: BitmapProvider,
    private val addPhotoToGallery: AddPhotoToGallery
) : StateEventsViewModel<PhotoConfirmationUiState, PhotoConfirmationUiEvents>(
    PhotoConfirmationUiState.Default,
    PhotoConfirmationUiEvents.Default
) {

    private var state = State()

    init {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            val uri = PhotoConfirmationDestination.argsFrom(savedStateHandle).photoUri
            val galleryId = PhotoConfirmationDestination.argsFrom(savedStateHandle).galleryId
            val photo = bitmapProvider.fromUri(uri)

            state = state.copy(photo = photo, galleryId = galleryId)
            updateState { copy(isLoading = false, photo = photo) }
        }
    }

    fun onPhotoDeclined() {
        updateEvents { copy(closeScreen = triggered) }
    }

    fun onPhotoAccepted() {
        viewModelScope.launch {
            updateEvents { copy(showPhotoConfirmation = triggered) }
        }
    }

    fun onPhotoConfirmationDeclined() {
        viewModelScope.launch {
            updateEvents { copy(hidePhotoConfirmation = triggered) }
        }
    }

    fun onPhotoConfirmationAccepted() {
        val photo = requireNotNull(state.photo)
        val galleryId = requireNotNull(state.galleryId)

        viewModelScope.launch {
            updateState {
                copy(
                    isUploadingPhoto = true,
                    uploadingMessage = Label.galleryPublishingPhotoInProgressText
                )
            }
            updateEvents { copy(hidePhotoConfirmation = triggered) }
            addPhotoToGallery(
                galleryId = galleryId,
                photo = photo
            ).onSuccess {
                updateState { copy(isUploadingPhoto = false, uploadingMessage = null) }
                updateEvents { copy(closeScreenWithSuccess = triggered) }
            }.onFailure {
                updateState {
                    copy(
                        isUploadingPhoto = false,
                        uploadingMessage = null,
                        alertData = AlertData.Default.copy(onDismiss = ::onAlertDismissed)
                    )
                }
            }
        }
    }

    fun onPhotoConfirmationShown() {
        viewModelScope.launch {
            updateEvents { copy(showPhotoConfirmation = consumed) }
        }
    }

    fun onPhotoConfirmationHidden() {
        viewModelScope.launch {
            updateEvents { copy(hidePhotoConfirmation = consumed) }
        }
    }

    fun onScreenClosed() {
        viewModelScope.launch {
            updateEvents { copy(closeScreen = consumed, closeScreenWithSuccess = consumed) }
        }
    }

    private fun onAlertDismissed() {
        viewModelScope.launch {
            updateState { copy(alertData = null) }
        }
    }

    private data class State(
        val photo: Bitmap? = null,
        val galleryId: String? = null
    )
}

internal data class PhotoConfirmationUiState(
    val isLoading: Boolean,
    val photo: Bitmap?,
    val isUploadingPhoto: Boolean,
    val uploadingMessage: String?,
    val alertData: AlertData?
) {
    companion object {
        val Default = PhotoConfirmationUiState(
            isLoading = false,
            photo = null,
            isUploadingPhoto = false,
            uploadingMessage = null,
            alertData = null
        )
    }
}

internal data class PhotoConfirmationUiEvents(
    val showPhotoConfirmation: StateEvent,
    val hidePhotoConfirmation: StateEvent,
    val closeScreen: StateEvent,
    val closeScreenWithSuccess: StateEvent
) {
    companion object {
        val Default = PhotoConfirmationUiEvents(
            showPhotoConfirmation = consumed,
            hidePhotoConfirmation = consumed,
            closeScreen = consumed,
            closeScreenWithSuccess = consumed
        )
    }
}
