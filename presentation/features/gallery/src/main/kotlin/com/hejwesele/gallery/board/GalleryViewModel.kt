package com.hejwesele.gallery.board

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateViewModel
import com.hejwesele.galleries.model.Gallery
import com.hejwesele.gallery.board.GalleryUiAction.*
import com.hejwesele.gallery.board.usecase.DismissGalleryHint
import com.hejwesele.gallery.board.usecase.GetEventSettings
import com.hejwesele.gallery.board.usecase.ObserveGallery
import com.hejwesele.settings.model.EventSettings
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

    private var state = ViewModelState()

    init {
        viewModelScope.launch {
            updateState { copy(loading = true) }
            getEventSettings()
                .onSuccess { settings ->
                    handleEventSettings(settings)
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
            val galleryId = requireNotNull(state.galleryId)
            updateState { copy(openImageCropper = triggered(galleryId)) }
        }
    }

    fun onPhotoUploadSuccess() {
        viewModelScope.launch {
            updateState { copy(showPhotoUploadSuccess = triggered) }
        }
    }

    fun onImageCropperOpened() {
        updateState { copy(openImageCropper = consumed()) }
    }

    fun onPhotoUploadSuccessShown() {
        updateState { copy(showPhotoUploadSuccess = consumed) }
    }

    private suspend fun handleEventSettings(settings: EventSettings) {
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
                observeGallery(galleryId)
                    .collect { result ->
                        result
                            .onSuccess { gallery -> showGalleryData(gallery) }
                            .onFailure { error -> showGalleryError(error) }
                    }
            } else {
                showGalleryDisabled()
            }
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
                galleryHintVisible = galleryHintEnabled,
                galleryLinkVisible = galleryLinkPresent,
                photos = photos.reversed()
            )
        }
    }

    private fun showGalleryError(error: Throwable) {
        updateState { copy(error = error) }
    }

    private fun showGalleryDisabled() {
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



    private data class ViewModelState(
        val eventDate: LocalDateTime? = null,
        val galleryId: String? = null,
        val hintDismissed: Boolean = false
    )
}

internal data class GalleryUiState(
    val openImageCropper: StateEventWithContent<String>,
    val showPhotoUploadSuccess: StateEvent,
    val enabled: Boolean,
    val loading: Boolean,
    val galleryHintVisible: Boolean,
    val galleryLinkVisible: Boolean,
    val photos: List<String>,
    val error: Throwable?
) {
    companion object {
        val DEFAULT = GalleryUiState(
            openImageCropper = consumed(),
            showPhotoUploadSuccess = consumed,
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
    class OpenImageCropperWithConfirmation(val galleryId: String) : GalleryUiAction()
    object ShowPhotoUploadSuccess : GalleryUiAction()
}
