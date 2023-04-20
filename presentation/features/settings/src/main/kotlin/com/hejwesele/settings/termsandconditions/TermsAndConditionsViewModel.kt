package com.hejwesele.settings.termsandconditions

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.components.ErrorData
import com.hejwesele.android.mvvm.StateEventsViewModel
import com.hejwesele.legaldocument.LegalPoint
import com.hejwesele.settings.usecase.GetTermsAndConditions
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TermsAndConditionsViewModel @Inject constructor(
    private val getTermsAndConditions: GetTermsAndConditions
) : StateEventsViewModel<TermsAndConditionsUiState, TermsAndConditionsUiEvents>(
    TermsAndConditionsUiState.Default,
    TermsAndConditionsUiEvents.Default
) {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            updateState { copy(isLoading = true) }

            getTermsAndConditions()
                .onSuccess { points ->
                    updateState {
                        copy(
                            isLoading = false,
                            legalPoints = points,
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

internal data class TermsAndConditionsUiState(
    val isLoading: Boolean,
    val legalPoints: List<LegalPoint>,
    val error: ErrorData?
) {
    companion object {
        val Default = TermsAndConditionsUiState(
            isLoading = false,
            legalPoints = emptyList(),
            error = null
        )
    }
}

internal data class TermsAndConditionsUiEvents(
    val navigateUp: StateEvent
) {
    companion object {
        val Default = TermsAndConditionsUiEvents(
            navigateUp = consumed
        )
    }
}
