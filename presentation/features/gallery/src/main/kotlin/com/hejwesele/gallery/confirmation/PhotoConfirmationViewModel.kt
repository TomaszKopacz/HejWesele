package com.hejwesele.gallery.confirmation

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hejwesele.android.components.DismissiveError
import com.hejwesele.android.mvvm.StateViewModel
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
) : StateViewModel<PhotoConfirmationUiState>(PhotoConfirmationUiState.DEFAULT) {

    private var state = InternalState()

    init {
        viewModelScope.launch {
            updateState { copy(loadingData = true) }

            val uri = PhotoConfirmationDestination.argsFrom(savedStateHandle).photoUri
            val galleryId = PhotoConfirmationDestination.argsFrom(savedStateHandle).galleryId
            val photo = bitmapProvider.fromUri(uri)

            state = state.copy(photo = photo, galleryId = galleryId)
            updateState { copy(loadingData = false, photo = photo) }
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
            updateState {
                copy(
                    uploadingPhoto = true,
                    uploadingMessage = Label.galleryPublishingPhotoInProgressText,
                    hidePhotoConfirmation = triggered
                )
            }
            addPhotoToGallery(
                galleryId = galleryId,
                photo = photo
            ).onSuccess {
                updateState {
                    copy(
                        uploadingPhoto = false,
                        uploadingMessage = null,
                        closeScreenWithSuccess = triggered
                    )
                }
            }.onFailure {
                updateState {
                    copy(
                        uploadingPhoto = false,
                        uploadingMessage = null,
                        dismissiveError = DismissiveError.Default.copy(onDismiss = ::onErrorDismissed)
                    )
                }
            }
        }
    }

    fun onPhotoConfirmationShown() {
        viewModelScope.launch {
            updateState { copy(showPhotoConfirmation = consumed) }
        }
    }

    fun onPhotoConfirmationHidden() {
        viewModelScope.launch {
            updateState { copy(hidePhotoConfirmation = consumed) }
        }
    }

    fun onErrorDismissed() {
        viewModelScope.launch {
            updateState { copy(dismissiveError = null) }
        }
    }

    fun onScreenClosed() {
        viewModelScope.launch {
            updateState {
                copy(
                    closeScreen = consumed,
                    closeScreenWithSuccess = consumed
                )
            }
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
    val closeScreenWithSuccess: StateEvent,
    val loadingData: Boolean,
    val photo: Bitmap?,
    val uploadingPhoto: Boolean,
    val uploadingMessage: String?,
    val dismissiveError: DismissiveError?
) {
    companion object {
        val DEFAULT = PhotoConfirmationUiState(
            showPhotoConfirmation = consumed,
            hidePhotoConfirmation = consumed,
            closeScreen = consumed,
            closeScreenWithSuccess = consumed,
            loadingData = false,
            photo = null,
            uploadingPhoto = false,
            uploadingMessage = null,
            dismissiveError = null
        )
    }
}
