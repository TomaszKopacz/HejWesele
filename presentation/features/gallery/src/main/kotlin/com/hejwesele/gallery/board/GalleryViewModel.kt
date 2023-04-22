package com.hejwesele.gallery.board

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.components.AlertData
import com.hejwesele.android.components.ErrorData
import com.hejwesele.android.mvvm.StateEventsViewModel
import com.hejwesele.android.theme.Label
import com.hejwesele.details.model.Details
import com.hejwesele.events.GetEventSettings
import com.hejwesele.events.model.EventSettings
import com.hejwesele.galleries.model.Gallery
import com.hejwesele.gallery.board.usecase.DismissGalleryHint
import com.hejwesele.gallery.board.usecase.ObserveDetails
import com.hejwesele.gallery.board.usecase.ObserveGallery
import com.hejwesele.intent.IntentData
import com.hejwesele.intent.IntentPackage.GOOGLE_DRIVE_PACKAGE
import com.hejwesele.intent.IntentType.GOOGLE_DRIVE
import com.hejwesele.result.extensions.merge
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
internal class GalleryViewModel @Inject constructor(
    private val getEventSettings: GetEventSettings,
    private val observeDetails: ObserveDetails,
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
            getEventSettings()
                .onSuccess { settings ->
                    val galleryId = settings.galleryId
                    if (galleryId != null) {
                        updateEvents { copy(openImageCropper = triggered(galleryId)) }
                    } else {
                        updateState { copy(alertData = AlertData.Default.copy(onDismiss = ::onAlertDismissed)) }
                    }
                }
                .onFailure {
                    handleEventSettingsError()
                }
        }
    }

    fun onImageCropError() {
        updateState { copy(alertData = AlertData.Default.copy(onDismiss = ::onAlertDismissed)) }
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
            hintDismissed = settings.galleryHintDismissed
        )

        val detailsId = settings.detailsId
        val galleryId = settings.galleryId

        if (galleryId != null) {
            val detailsFlow = observeDetails(detailsId)
            val galleryFlow = observeGallery(galleryId)

            detailsFlow
                .combine(galleryFlow) { detailsResult, galleryResult ->
                    detailsResult.merge(galleryResult) { details, gallery -> details to gallery }
                }
                .collect { result ->
                    result
                        .onSuccess { (details, gallery) ->
                            state = state.copy(externalGalleryUrl = gallery.externalGallery)
                            sendGalleryContentState(details, gallery)
                        }
                        .onFailure {
                            sendGalleryErrorState()
                        }
                }
        } else {
            sendGalleryDisabledState()
        }
    }

    private fun sendGalleryContentState(details: Details, gallery: Gallery) {
        val weddingDate = details.date
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
                errorData = null
            )
        }
    }

    private fun sendGalleryErrorState() {
        updateState { copy(isLoading = false, errorData = ErrorData.Default) }
    }

    private fun sendGalleryDisabledState() {
        updateState {
            copy(
                isEnabled = false,
                isLoading = false,
                galleryHintEnabled = false,
                externalGalleryEnabled = false,
                photos = arrayListOf(),
                errorData = null
            )
        }
    }

    private fun handleEventSettingsError() {
        updateState {
            copy(
                isLoading = false,
                alertData = AlertData.Default.copy(
                    title = Label.errorDescriptionEventNotFoundText,
                    onDismiss = ::onEventNotFoundAlertDismissed
                )
            )
        }
    }

    private fun onEventNotFoundAlertDismissed() {
        updateState { copy(alertData = null) }
        updateEvents { copy(openLogin = triggered) }
    }

    private fun onAlertDismissed() {
        updateState { copy(alertData = null) }
    }

    private data class State(
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
    val errorData: ErrorData?,
    val alertData: AlertData?
) {
    companion object {
        val Default = GalleryUiState(
            isEnabled = true,
            isLoading = false,
            contentActive = false,
            galleryHintEnabled = true,
            externalGalleryEnabled = false,
            photos = arrayListOf(),
            errorData = null,
            alertData = null
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
