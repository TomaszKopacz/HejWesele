package com.hejwesele.gallery.board

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.canhub.cropper.CropImageView.CropResult
import com.hejwesele.android.mvvm.StateViewModel
import com.hejwesele.extensions.BitmapResolver
import com.hejwesele.galleries.model.Gallery
import com.hejwesele.gallery.board.GalleryUiAction.OpenDeviceGallery
import com.hejwesele.gallery.board.GalleryUiAction.OpenImageCropper
import com.hejwesele.gallery.board.usecase.AddPhotoToGallery
import com.hejwesele.gallery.board.usecase.DismissGalleryHint
import com.hejwesele.gallery.board.usecase.GetEventSettings
import com.hejwesele.gallery.board.usecase.ObserveGallery
import com.hejwesele.result.GeneralError
import com.hejwesele.settings.model.EventSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
internal class GalleryViewModel @Inject constructor(
    private val getEventSettings: GetEventSettings,
    private val observeGallery: ObserveGallery,
    private val dismissGalleryHint: DismissGalleryHint,
    private val uploadPhoto: AddPhotoToGallery,
    private val bitmapResolver: BitmapResolver
) : StateViewModel<GalleryUiState>(GalleryUiState.DEFAULT) {

    private var state = ViewModelState()

    init {
        viewModelScope.launch {
            updateState { copy(loading = true) }
            getEventSettings()
                .onSuccess { settings ->
                    handleStoredEventSettings(settings)
                }
                .onFailure {
                    /* show error and logout */
                }
        }
    }

    fun onGalleryHintDismissed() {
        viewModelScope.launch {
            dismissGalleryHint()
                .onSuccess {
                    state = state.copy(hintDismissed = true)
                    updateState { copy(galleryHintVisible = false) }
                }
        }
    }

    fun onAddPhotoClicked() {
        viewModelScope.launch {
            updateState { copy(action = OpenDeviceGallery(IMAGE_DIRECTORY)) }
        }
    }

    fun onImageSelected(uri: Uri?) {
        viewModelScope.launch {
            if (uri != null) {
                updateState { copy(action = OpenImageCropper(uri)) }
            } else {
                // TODO - show error
            }
        }
    }

    fun onImageCropped(result: CropResult) {
        viewModelScope.launch {
            val uri = result.uriContent
            if (result.isSuccessful && uri != null) {
                uploadCroppedImage(bitmapResolver.getBitmap(uri))
            } else {
                updateState { copy(error = GeneralError(null)) }
            }
        }
    }

    fun onActionConsumed() {
        updateState { copy(action = null) }
    }

    private suspend fun handleStoredEventSettings(settings: EventSettings) {
        val event = settings.event

        if (event == null) {
            // TODO - logout
        } else {
            state = state.copy(
                eventDate = event.date,
                galleryId = event.galleryId,
                hintDismissed = settings.galleryHintDismissed
            )

            val galleryId = event.galleryId
            if (galleryId != null) {
                observeEventGallery(galleryId)
            } else {
                handleGalleryDisabled()
            }
        }
    }

    private suspend fun observeEventGallery(galleryId: String) {
        observeGallery(galleryId)
            .collect { result ->
                result
                    .onSuccess { gallery -> handleObservedGallery(gallery) }
                    .onFailure { error -> handleObservedGalleryError(error) }
            }
    }

    private fun handleObservedGallery(gallery: Gallery) {
        val weddingDate = requireNotNull(state.eventDate)
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val weddingStarted = now >= weddingDate
        val galleryLinkPresent = !gallery.externalGallery.isNullOrEmpty()
        val galleryHintEnabled = weddingStarted && !state.hintDismissed && !galleryLinkPresent
        val photos = gallery.photos

        updateState {
            copy(
                enabled = true,
                loading = false,
                galleryHintVisible = galleryHintEnabled,
                galleryLinkVisible = galleryLinkPresent,
                photos = photos.reversed()
            )
        }
    }

    private fun handleObservedGalleryError(error: Throwable) {
        updateState { copy(error = error) }
    }

    private fun handleGalleryDisabled() {
        updateState {
            copy(
                enabled = false,
                loading = false,
                galleryHintVisible = false,
                galleryLinkVisible = false,
                photos = emptyList(),
            )
        }
    }

    private suspend fun uploadCroppedImage(bitmap: Bitmap) {
        val galleryId = state.galleryId

        if (galleryId != null) {
            uploadPhoto(
                galleryId = galleryId,
                photo = bitmap
            )
                .onSuccess {
                    // TODO - show success
                }
                .onFailure {
                    // TODO - show error
                }
        }
    }

    private data class ViewModelState(
        val eventDate: LocalDateTime? = null,
        val galleryId: String? = null,
        val hintDismissed: Boolean = false
    )

    companion object {
        private const val IMAGE_DIRECTORY = "image/*"
    }
}

internal data class GalleryUiState(
    val action: GalleryUiAction?,
    val enabled: Boolean,
    val loading: Boolean,
    val galleryHintVisible: Boolean,
    val galleryLinkVisible: Boolean,
    val photos: List<String>,
    val error: Throwable?
) {
    companion object {
        val DEFAULT = GalleryUiState(
            action = null,
            enabled = true,
            loading = false,
            galleryHintVisible = true,
            galleryLinkVisible = false,
            photos = emptyList(),
            error = null
        )
    }
}

internal sealed class GalleryUiAction {
    class OpenDeviceGallery(val directory: String) : GalleryUiAction()
    class OpenImageCropper(val imageUri: Uri) : GalleryUiAction()
}
