package com.hejwesele.gallery.board

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.components.DismissiveError
import com.hejwesele.android.components.PermanentError
import com.hejwesele.android.mvvm.StateEventsViewModel
import com.hejwesele.android.theme.Label
import com.hejwesele.events.GetEventSettings
import com.hejwesele.events.model.EventSettings
import com.hejwesele.gallery.board.usecase.DismissGalleryHint
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
) : StateEventsViewModel<GalleryUiState, GalleryUiEvents>(GalleryUiState.Default, GalleryUiEvents.Default) {

    private var state = State()

    init {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            getEventSettings()
                .onSuccess { settings ->
                    handleEventSettings(settings)
                }
                .onFailure {
                    handleEventSettingsError()
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
                updateEvents { copy(openExternalGallery = triggered(intent)) }
            }
        }
    }

    fun onAddPhotoClicked() {
        viewModelScope.launch {
            val galleryId = requireNotNull(state.galleryId)
            updateEvents { copy(openImageCropper = triggered(galleryId)) }
        }
    }

    fun onImageCropError() {
        updateState {
            copy(
                dismissiveError = DismissiveError.Default.copy(
                    onDismiss = ::onImageCropErrorDismissed
                )
            )
        }
    }

    fun onPhotoUploadSuccess() {
        viewModelScope.launch {
            updateEvents { copy(showPhotoUploadSuccess = triggered) }
        }
    }

    fun onExternalGalleryOpened() {
        viewModelScope.launch {
            updateEvents { copy(openExternalGallery = consumed()) }
        }
    }

    fun onImageCropperOpened() {
        updateEvents { copy(openImageCropper = consumed()) }
    }

    fun onPhotoUploadSuccessShown() {
        updateEvents { copy(showPhotoUploadSuccess = consumed) }
    }

    fun onLoginOpened() {
        viewModelScope.launch {
            updateEvents { copy(openLogin = consumed) }
        }
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

                            val weddingDate = requireNotNull(state.eventDate)
                            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                            val weddingStarted = now >= weddingDate
                            val galleryLinkPresent = !gallery.externalGallery.isNullOrEmpty()
                            val galleryHintEnabled = weddingStarted && !state.hintDismissed && !galleryLinkPresent
                            val photos = gallery.photos

                            updateState {
                                copy(
                                    isEnabled = true,
                                    isLoading = false,
                                    contentActive = weddingStarted,
                                    galleryHintEnabled = galleryHintEnabled,
                                    externalGalleryEnabled = galleryLinkPresent,
                                    photos = ArrayList(photos.reversed()),
                                    permanentError = null
                                )
                            }
                        }
                        .onFailure {
                            updateState {
                                copy(
                                    isLoading = false,
                                    permanentError = PermanentError.Default
                                )
                            }
                        }
                }
        } else {
            updateState {
                copy(
                    isEnabled = false,
                    isLoading = false,
                    galleryHintEnabled = false,
                    externalGalleryEnabled = false,
                    photos = arrayListOf(),
                    permanentError = null
                )
            }
        }
    }

    private fun handleEventSettingsError() {
        updateState {
            copy(
                isLoading = false,
                dismissiveError = DismissiveError.Default.copy(
                    title = Label.errorDescriptionEventNotFoundText,
                    onDismiss = ::onEventNotFoundErrorDismissed
                )
            )
        }
    }

    private fun onEventNotFoundErrorDismissed() {
        updateState { copy(dismissiveError = null) }
        updateEvents { copy(openLogin = triggered) }
    }

    private fun onImageCropErrorDismissed() {
        updateState { copy(dismissiveError = null) }
    }

    private data class State(
        val eventDate: LocalDateTime? = null,
        val galleryId: String? = null,
        val hintDismissed: Boolean = false,
        val externalGalleryUrl: String? = null
    )
}

internal data class GalleryUiState(
    val isEnabled: Boolean,
    val isLoading: Boolean,
    val contentActive: Boolean,
    val galleryHintEnabled: Boolean,
    val externalGalleryEnabled: Boolean,
    val photos: ArrayList<String>,
    val permanentError: PermanentError?,
    val dismissiveError: DismissiveError?
) {
    companion object {
        val Default = GalleryUiState(
            isEnabled = true,
            isLoading = false,
            contentActive = false,
            galleryHintEnabled = true,
            externalGalleryEnabled = false,
            photos = arrayListOf(),
            permanentError = null,
            dismissiveError = null
        )
    }
}

internal data class GalleryUiEvents(
    val openExternalGallery: StateEventWithContent<IntentData>,
    val openImageCropper: StateEventWithContent<String>,
    val showPhotoUploadSuccess: StateEvent,
    val openLogin: StateEvent
) {
    companion object {
        val Default = GalleryUiEvents(
            openExternalGallery = consumed(),
            openImageCropper = consumed(),
            showPhotoUploadSuccess = consumed,
            openLogin = consumed
        )
    }
}
