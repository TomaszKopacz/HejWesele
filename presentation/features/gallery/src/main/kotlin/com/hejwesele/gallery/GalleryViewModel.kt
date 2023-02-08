package com.hejwesele.gallery

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateActionsViewModel
import com.hejwesele.galleries.model.Gallery
import com.hejwesele.gallery.model.GalleryUiAction
import com.hejwesele.gallery.model.GalleryUiState
import com.hejwesele.gallery.usecase.DismissGalleryHint
import com.hejwesele.gallery.usecase.GetEventSettings
import com.hejwesele.gallery.usecase.ObserveGallery
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
) : StateActionsViewModel<GalleryUiState, GalleryUiAction>(GalleryUiState.DEFAULT) {

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

    private suspend fun handleStoredEvent(
        settings: EventSettings,
    ) {
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

    fun onAddClicked() {
        viewModelScope.launch {

        }
    }
}
