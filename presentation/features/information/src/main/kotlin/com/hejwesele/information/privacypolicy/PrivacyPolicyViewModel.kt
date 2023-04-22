package com.hejwesele.information.privacypolicy

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.components.ErrorData
import com.hejwesele.android.mvvm.StateEventsViewModel
import com.hejwesele.information.usecase.GetPrivacyPolicy
import com.hejwesele.legaldocument.LegalPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PrivacyPolicyViewModel @Inject constructor(
    private val getPrivacyPolicy: GetPrivacyPolicy
) : StateEventsViewModel<PrivacyPolicyUiState, PrivacyPolicyUiEvents>(PrivacyPolicyUiState.Default, PrivacyPolicyUiEvents.Default) {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            updateState { copy(isLoading = true) }

            getPrivacyPolicy()
                .onSuccess { legalPoints ->
                    updateState {
                        copy(
                            isLoading = false,
                            legalPoints = legalPoints,
                            error = null
                        )
                    }
                }
                .onFailure {
                    updateState {
                        copy(
                            isLoading = false,
                            legalPoints = emptyList(),
                            error = ErrorData.Default
                        )
                    }
                }
        }
    }

    fun onBack() {
        viewModelScope.launch {
            updateEvents { copy(navigateUp = triggered) }
        }
    }

    fun onNavigatedUp() {
        viewModelScope.launch {
            updateEvents { copy(navigateUp = consumed) }
        }
    }
}

internal data class PrivacyPolicyUiState(
    val isLoading: Boolean,
    val legalPoints: List<LegalPoint>,
    val error: ErrorData?
) {
    companion object {
        val Default = PrivacyPolicyUiState(
            isLoading = false,
            legalPoints = emptyList(),
            error = null
        )
    }
}

internal data class PrivacyPolicyUiEvents(
    val navigateUp: StateEvent
) {
    companion object {
        val Default = PrivacyPolicyUiEvents(
            navigateUp = consumed
        )
    }
}
