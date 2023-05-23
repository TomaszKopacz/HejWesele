package com.hejwesele.services.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hejwesele.android.components.AlertData
import com.hejwesele.android.mvvm.StateEventsViewModel
import com.hejwesele.android.theme.Label
import com.hejwesele.intent.IntentData
import com.hejwesele.intent.IntentType
import com.hejwesele.services.details.destinations.ServiceDetailsDestination
import com.hejwesele.services.model.IntentUiModel
import com.hejwesele.services.model.Service
import com.hejwesele.services.model.ServiceDetailsUiModel
import com.hejwesele.services.usecase.GetService
import com.hejwesele.theme.R
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ServiceDetailsViewModel @Inject constructor(
    state: SavedStateHandle,
    val getService: GetService
) : StateEventsViewModel<ServiceDetailsUiState, ServicesDetailsUiEvents>(
    ServiceDetailsUiState.Default,
    ServicesDetailsUiEvents.Default
) {

    init {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            val serviceId = ServiceDetailsDestination.argsFrom(state).serviceId
            getService(serviceId)
                .onSuccess { service -> emitSuccessState(service.toUiModel()) }
                .onFailure { emitNotFoundState() }
        }
    }

    fun onIntentSelected(intent: IntentUiModel) {
        updateEvents {
            copy(openIntent = triggered(intent))
        }
    }

    fun onGoBack() {
        updateEvents {
            copy(goBack = triggered)
        }
    }

    fun onIntentOpened() {
        viewModelScope.launch {
            updateEvents { copy(openIntent = consumed()) }
        }
    }

    fun onNavigatedBack() {
        viewModelScope.launch {
            updateEvents { copy(goBack = consumed) }
        }
    }

    private fun onServiceNotFoundAlertDismissed() {
        updateEvents {
            copy(goBack = triggered)
        }
    }

    private fun emitSuccessState(service: ServiceDetailsUiModel) {
        updateState {
            copy(
                isLoading = false,
                service = service,
                alertData = null
            )
        }
    }

    private fun emitNotFoundState() {
        updateState {
            copy(
                isLoading = false,
                service = null,
                alertData = AlertData.Default.copy(
                    title = Label.errorDescriptionServiceNotFoundText,
                    onDismiss = ::onServiceNotFoundAlertDismissed
                )
            )
        }
    }

    private fun Service.toUiModel() = ServiceDetailsUiModel(
        name = name ?: title,
        imageUrl = image,
        details = details.map {
            it.title to it.content
        },
        intents = intents.map { it.toUiModel() }
    )

    private fun IntentData.toUiModel() = IntentUiModel(
        iconResId = getIntentIconResId(intentType),
        intentPackage = intentPackage,
        url = intentUrl
    )

    private fun getIntentIconResId(type: IntentType) = when (type) {
        IntentType.INSTAGRAM -> R.drawable.ic_instagram_primary
        IntentType.GOOGLE_MAPS -> R.drawable.ic_maps_primary
        IntentType.WWW -> R.drawable.ic_web_primary
        else -> R.drawable.ic_web_primary
    }
}

internal data class ServiceDetailsUiState(
    val isLoading: Boolean,
    val service: ServiceDetailsUiModel?,
    val alertData: AlertData?
) {
    companion object {
        val Default = ServiceDetailsUiState(
            isLoading = false,
            service = null,
            alertData = null
        )
    }
}

internal data class ServicesDetailsUiEvents(
    val openIntent: StateEventWithContent<IntentUiModel>,
    val goBack: StateEvent
) {
    companion object {
        val Default = ServicesDetailsUiEvents(
            openIntent = consumed(),
            goBack = consumed
        )
    }
}
