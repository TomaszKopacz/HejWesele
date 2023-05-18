package com.hejwesele.services.board

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.components.AlertData
import com.hejwesele.android.components.ErrorData
import com.hejwesele.android.mvvm.StateEventsViewModel
import com.hejwesele.android.theme.Label
import com.hejwesele.events.model.EventSettings
import com.hejwesele.services.R
import com.hejwesele.services.model.MaterialServiceColor
import com.hejwesele.services.model.Service
import com.hejwesele.services.model.ServiceListItem
import com.hejwesele.services.model.ServiceType
import com.hejwesele.services.model.ServiceType.DRINK
import com.hejwesele.services.model.ServiceType.FOOD
import com.hejwesele.services.model.ServiceType.INSTAX
import com.hejwesele.services.model.ServiceType.MOVIE
import com.hejwesele.services.model.ServiceType.MUSIC
import com.hejwesele.services.model.ServiceType.PHOTO
import com.hejwesele.services.model.ServiceType.VENUE
import com.hejwesele.services.model.ServiceUiModel
import com.hejwesele.services.model.Services
import com.hejwesele.services.usecase.GetEventSettings
import com.hejwesele.services.usecase.ObserveServices
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ServicesViewModel @Inject constructor(
    val getEventSettings: GetEventSettings,
    val observeServices: ObserveServices
) :
    StateEventsViewModel<ServicesUiState, ServicesUiEvents>(ServicesUiState.Default, ServicesUiEvents.Default) {

    init {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            getEventSettings()
                .onSuccess { settings -> handleEventSettings(settings) }
                .onFailure { handleEventSettingsError() }
        }
    }

    fun onServiceSelected(service: ServiceUiModel) {
        viewModelScope.launch {
            updateEvents { copy(openServiceDetails = triggered(service)) }
        }
    }

    fun onServiceDetailsOpened() {
        updateEvents { copy(openServiceDetails = consumed()) }
    }

    fun onLogoutPerformed() {
        updateEvents { copy(logout = consumed) }
    }

    private suspend fun handleEventSettings(settings: EventSettings) {
        val servicesId = settings.servicesId

        if (servicesId != null) {
            observeServices(servicesId).collect { result ->
                result
                    .onSuccess { services -> handleServicesSuccessResult(services) }
                    .onFailure { emitErrorState() }
            }
        } else {
            emitDisabledState()
        }
    }

    private fun handleServicesSuccessResult(services: Services) {
        updateState {
            copy(
                isLoading = false,
                isEnabled = true,
                services = prepareServicesUiModels(services),
                errorData = null
            )
        }
    }

    private fun prepareServicesUiModels(services: Services): List<ServiceListItem> {
        val partnersLabel = Label.servicesPartnersText.toListItem()
        val attractionsLabel = Label.servicesAttractionsText.toListItem()
        val partners = services.partners.toListItems()
        val attractions = services.attractions.toListItems()

        return partnersLabel + partners + attractionsLabel + attractions
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
        updateEvents { copy(logout = triggered) }
    }

    private fun emitDisabledState() {
        updateState {
            copy(
                isLoading = false,
                isEnabled = false,
                services = emptyList(),
                errorData = null,
                alertData = null
            )
        }
    }

    private fun emitErrorState() {
        updateState {
            copy(
                isLoading = false,
                isEnabled = false,
                services = emptyList(),
                errorData = ErrorData.Default
            )
        }
    }

    private fun String.toListItem() = listOf(ServiceListItem.Label(text = this))
    private fun List<Service>.toListItems() = map { service ->
        ServiceListItem.Tile(
            service = ServiceUiModel(
                title = service.title,
                name = service.name,
                description = service.description,
                color = MaterialServiceColor.values().random(),
                animation = service.type.getAnimationResId()
            )
        )
    }

    private fun ServiceType.getAnimationResId() = when (this) {
        VENUE -> R.raw.lottie_party
        MUSIC -> R.raw.lottie_dj
        PHOTO -> R.raw.lottie_photographer
        MOVIE -> R.raw.lottie_video
        FOOD -> R.raw.lottie_drink
        DRINK -> R.raw.lottie_drink
        INSTAX -> R.raw.lottie_instax
    }
}

internal data class ServicesUiState(
    val isLoading: Boolean,
    val isEnabled: Boolean,
    val services: List<ServiceListItem>,
    val errorData: ErrorData?,
    val alertData: AlertData?
) {
    companion object {
        val Default = ServicesUiState(
            isLoading = false,
            isEnabled = true,
            services = emptyList(),
            errorData = null,
            alertData = null
        )
    }
}

internal data class ServicesUiEvents(
    val openServiceDetails: StateEventWithContent<ServiceUiModel>,
    val logout: StateEvent
) {
    companion object {
        val Default = ServicesUiEvents(
            openServiceDetails = consumed(),
            logout = consumed
        )
    }
}
