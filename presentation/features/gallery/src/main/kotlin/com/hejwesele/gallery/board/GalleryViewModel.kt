package com.hejwesele.gallery.board

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView.CropResult
import com.canhub.cropper.CropImageView.CropShape
import com.hejwesele.android.mvvm.StateViewModel
import com.hejwesele.extensions.BitmapResolver
import com.hejwesele.galleries.model.Gallery
import com.hejwesele.gallery.board.GalleryUiAction.OpenDeviceGallery
import com.hejwesele.gallery.board.GalleryUiAction.OpenImageCropper
import com.hejwesele.gallery.board.usecase.DismissGalleryHint
import com.hejwesele.gallery.board.usecase.GetEventSettings
import com.hejwesele.gallery.board.usecase.ObserveGallery
import com.hejwesele.result.GeneralError
import com.hejwesele.result.onCompleted
import com.hejwesele.result.onError
import com.hejwesele.result.onSuccess
import com.hejwesele.settings.model.EventSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
internal class GalleryViewModel @Inject constructor(
    private val getEventSettings: GetEventSettings,
    private val observeGallery: ObserveGallery,
    private val dismissGalleryHint: DismissGalleryHint,
    private val bitmapResolver: BitmapResolver
) : StateViewModel<GalleryUiState>(GalleryUiState.DEFAULT) {

    init {
        viewModelScope.launch {
            updateState { copy(loading = true) }
            getEventSettings()
                .onSuccess { settings ->
                    when (settings.event) {
                        null -> { /* show error and logout */ }
                        else -> handleStoredEvent(settings)
                    }
                }
                .onError {
                    /* show error and logout */
                }
        }
    }

    private suspend fun handleStoredEvent(settings: EventSettings) {
        val event = settings.event
        requireNotNull(event)

        event.galleryId?.let {
            observeEventGallery(settings, it)
        }
    }

    private suspend fun observeEventGallery(
        settings: EventSettings,
        galleryId: String
    ) {
        observeGallery(galleryId)
            .collect { result ->
                result
                    .onSuccess { gallery -> handleGalleryData(settings, gallery) }
                    .onError { error -> handleGalleryError(error) }
            }
    }

    private fun handleGalleryData(settings: EventSettings, gallery: Gallery) {
        val weddingDate = requireNotNull(settings.event).date
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val weddingStarted = now >= weddingDate
        val galleryHintDismissed = settings.galleryHintDismissed
        val galleryLinkPresent = !gallery.externalGallery.isNullOrEmpty()
        val galleryHintEnabled = weddingStarted && !galleryHintDismissed && !galleryLinkPresent
        val photos = gallery.photos

        updateState {
            copy(
                loading = false,
                galleryHintVisible = galleryHintEnabled,
                galleryLinkVisible = galleryLinkPresent,
                photos = photos
            )
        }
    }

    private fun handleGalleryError(error: GeneralError) {
        updateState { copy(error = error) }
    }

    fun onGalleryHintDismissed() {
        viewModelScope.launch {
            dismissGalleryHint()
                .onCompleted {
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
                val options = CropImageContractOptions(
                    uri = uri,
                    cropImageOptions = cropOptions
                )
                updateState { copy(action = OpenImageCropper(options)) }
            }
        }
    }

    @Suppress("UnusedPrivateMember")
    fun onImageCropped(result: CropResult) {
        viewModelScope.launch {
            val uri = result.uriContent
            if (result.isSuccessful && uri != null) {
                val bitmap = bitmapResolver.getBitmap(uri)
            } else {
                updateState { copy(error = GeneralError(null, null)) }
            }
        }
    }

    fun onActionConsumed() {
        updateState { copy(action = null) }
    }

    companion object {
        private const val IMAGE_DIRECTORY = "image/*"
        private val cropOptions = CropImageOptions(
            cropShape = CropShape.RECTANGLE,
            fixAspectRatio = true,
            aspectRatioX = 1,
            aspectRatioY = 1
        )
    }
}

internal data class GalleryUiState(
    val action: GalleryUiAction?,
    val loading: Boolean,
    val galleryHintVisible: Boolean,
    val galleryLinkVisible: Boolean,
    val photos: List<String>,
    val error: GeneralError?
) {
    companion object {
        val DEFAULT = GalleryUiState(
            action = null,
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
    class OpenImageCropper(val options: CropImageContractOptions) : GalleryUiAction()
}
