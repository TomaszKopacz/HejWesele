package com.hejwesele.gallery.board

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateViewModel
import com.hejwesele.events.model.EventSettings
import com.hejwesele.galleries.model.Gallery
import com.hejwesele.gallery.board.usecase.DismissGalleryHint
import com.hejwesele.gallery.board.usecase.GetEventSettings
import com.hejwesele.gallery.board.usecase.ObserveGallery
import com.hejwesele.intent.IntentData
import com.hejwesele.intent.IntentPackage.GOOGLE_DRIVE_PACKAGE
import com.hejwesele.intent.IntentType.GOOGLE_DRIVE
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
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
    private val dismissGalleryHint: DismissGalleryHint
) : StateViewModel<GalleryUiState>(GalleryUiState.DEFAULT) {

    private var state = State()

    init {
        viewModelScope.launch {
            updateState { copy(loading = true) }
            getEventSettings()
                .onSuccess { settings ->
                    handleEventSettings(settings)
                }
                .onFailure {
                    /* show error dialog and logout */
                }
        }
    }

    fun onGalleryHintDismissed() {
        viewModelScope.launch {
            dismissGalleryHint()
                .onSuccess {
                    state = state.copy(hintDismissed = true)
                    updateState { copy(galleryHintEnabled = false) }
                }
        }
    }

    fun onGalleryLinkClicked() {
        viewModelScope.launch {
            val externalGalleryUrl = state.externalGalleryUrl
            if (externalGalleryUrl != null) {
                val intent = IntentData(
                    intentType = GOOGLE_DRIVE,
                    intentPackage = GOOGLE_DRIVE_PACKAGE,
                    intentUrl = externalGalleryUrl
                )
                updateState {
                    copy(openExternalGallery = triggered(intent))
                }
            }
        }
    }

    fun onExternalGalleryOpened() {
        viewModelScope.launch {
            updateState {
                copy(openExternalGallery = consumed())
            }
        }
    }

    fun onAddPhotoClicked() {
        viewModelScope.launch {
            val galleryId = requireNotNull(state.galleryId)
            updateState { copy(openImageCropper = triggered(galleryId)) }
        }
    }

    fun onImageCropperOpened() {
        updateState { copy(openImageCropper = consumed()) }
    }

    fun onImageCropError() {
        updateState { copy(imageCropFailure = true) }
    }

    fun onImageCropErrorDismissed() {
        updateState { copy(imageCropFailure = false) }
    }

    fun onPhotoUploadSuccess() {
        viewModelScope.launch {
            updateState { copy(showPhotoUploadSuccess = triggered) }
        }
    }

    fun onPhotoUploadSuccessShown() {
        updateState { copy(showPhotoUploadSuccess = consumed) }
    }

    fun onErrorRetry() {
        /* no-op */
    }

    private suspend fun handleEventSettings(settings: EventSettings) {
        state = state.copy(
            eventDate = settings.date,
            galleryId = settings.galleryId,
            hintDismissed = settings.galleryHintDismissed
        )

        val galleryId = settings.galleryId
        if (galleryId != null) {
            observeGallery(galleryId)
                .collect { result ->
                    result
                        .onSuccess { gallery ->
                            state = state.copy(externalGalleryUrl = gallery.externalGallery)
                            showGalleryData(gallery)
                        }
                        .onFailure { error -> showGalleryError(error) }
                }
        } else {
            showGalleryDisabled()
        }
    }

    private fun showGalleryData(gallery: Gallery) {
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
                weddingStarted = weddingStarted,
                galleryHintEnabled = galleryHintEnabled,
                externalGalleryEnabled = galleryLinkPresent,
                photos = ArrayList(photos.reversed()),
                imageCropFailure = false,
                error = null
            )
        }
    }

    private fun showGalleryError(error: Throwable) {
        updateState {
            copy(
                loading = false,
                error = error
            )
        }
    }

    private fun showGalleryDisabled() {
        updateState {
            copy(
                enabled = false,
                loading = false,
                galleryHintEnabled = false,
                externalGalleryEnabled = false,
                photos = arrayListOf(),
                imageCropFailure = false,
                error = null
            )
        }
    }

    private data class State(
        val eventDate: LocalDateTime? = null,
        val galleryId: String? = null,
        val hintDismissed: Boolean = false,
        val externalGalleryUrl: String? = null
    )
}

internal data class GalleryUiState(
    val openExternalGallery: StateEventWithContent<IntentData>,
    val openImageCropper: StateEventWithContent<String>,
    val showPhotoUploadSuccess: StateEvent,
    val enabled: Boolean,
    val loading: Boolean,
    val weddingStarted: Boolean,
    val galleryHintEnabled: Boolean,
    val externalGalleryEnabled: Boolean,
    val photos: ArrayList<String>,
    val imageCropFailure: Boolean,
    val error: Throwable?
) {
    companion object {
        val DEFAULT = GalleryUiState(
            openExternalGallery = consumed(),
            openImageCropper = consumed(),
            showPhotoUploadSuccess = consumed,
            enabled = true,
            loading = false,
            weddingStarted = false,
            galleryHintEnabled = true,
            externalGalleryEnabled = false,
            photos = arrayListOf(),
            imageCropFailure = false,
            error = null
        )
    }
}
