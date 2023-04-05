package com.hejwesele.settings.overview

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SettingsOverviewViewModel @Inject constructor() :
    StateViewModel<SettingsOverviewUiState>(SettingsOverviewUiState.DEFAULT) {

    fun onBack() {
        viewModelScope.launch {
            updateState { copy(navigateUp = triggered) }
        }
    }

    fun onTermsAndConditionsRequested() {
        viewModelScope.launch {
            updateState { copy(openTermsAndConditions = triggered) }
        }
    }

    fun onPrivacyPolicyRequested() {
        viewModelScope.launch {
            updateState { copy(openPrivacyPolicy = triggered) }
        }
    }

    fun onNavigatedUp() {
        viewModelScope.launch {
            updateState { copy(navigateUp = consumed) }
        }
    }

    fun onTermsAndConditionsOpened() {
        viewModelScope.launch {
            updateState { copy(openTermsAndConditions = consumed) }
        }
    }

    fun onDataPrivacyOpened() {
        viewModelScope.launch {
            updateState { copy(openPrivacyPolicy = consumed) }
        }
    }
}

internal data class SettingsOverviewUiState(
    val navigateUp: StateEvent,
    val openTermsAndConditions: StateEvent,
    val openPrivacyPolicy: StateEvent
) {
    companion object {
        val DEFAULT = SettingsOverviewUiState(
            navigateUp = consumed,
            openTermsAndConditions = consumed,
            openPrivacyPolicy = consumed
        )
    }
}
