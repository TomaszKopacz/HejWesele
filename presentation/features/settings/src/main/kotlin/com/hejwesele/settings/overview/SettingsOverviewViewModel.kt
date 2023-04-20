package com.hejwesele.settings.overview

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateEventsViewModel
import com.hejwesele.settings.usecase.GetAppVersion
import com.hejwesele.settings.usecase.GetContactEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SettingsOverviewViewModel @Inject constructor(
    private val getContactEmail: GetContactEmail,
    private val getAppVersion: GetAppVersion
) : StateEventsViewModel<SettingsOverviewUiState, SettingsOverviewUiEvents>(
    SettingsOverviewUiState.Default,
    SettingsOverviewUiEvents.Default
) {

    init {
        viewModelScope.launch {
            updateState {
                copy(
                    contactEmail = getContactEmail(),
                    appVersion = getAppVersion()
                )
            }
        }
    }

    fun onBack() {
        viewModelScope.launch {
            updateEvents { copy(navigateUp = triggered) }
        }
    }

    fun onTermsAndConditionsRequested() {
        viewModelScope.launch {
            updateEvents { copy(openTermsAndConditions = triggered) }
        }
    }

    fun onPrivacyPolicyRequested() {
        viewModelScope.launch {
            updateEvents { copy(openPrivacyPolicy = triggered) }
        }
    }

    fun onNavigatedUp() {
        viewModelScope.launch {
            updateEvents { copy(navigateUp = consumed) }
        }
    }

    fun onTermsAndConditionsOpened() {
        viewModelScope.launch {
            updateEvents { copy(openTermsAndConditions = consumed) }
        }
    }

    fun onDataPrivacyOpened() {
        viewModelScope.launch {
            updateEvents { copy(openPrivacyPolicy = consumed) }
        }
    }
}

internal data class SettingsOverviewUiState(
    val contactEmail: String,
    val appVersion: String
) {
    companion object {
        val Default = SettingsOverviewUiState(
            contactEmail = "",
            appVersion = ""
        )
    }
}

internal data class SettingsOverviewUiEvents(
    val navigateUp: StateEvent,
    val openTermsAndConditions: StateEvent,
    val openPrivacyPolicy: StateEvent
) {
    companion object {
        val Default = SettingsOverviewUiEvents(
            navigateUp = consumed,
            openTermsAndConditions = consumed,
            openPrivacyPolicy = consumed
        )
    }
}
