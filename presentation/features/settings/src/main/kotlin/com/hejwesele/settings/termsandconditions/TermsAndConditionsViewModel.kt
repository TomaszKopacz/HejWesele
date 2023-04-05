package com.hejwesele.settings.termsandconditions

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateViewModel
import com.hejwesele.regulations.model.RegulationPoint
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
) : StateViewModel<TermsAndConditionsUiState>(TermsAndConditionsUiState.DEFAULT) {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            updateState { copy(isLoading = true) }

            getTermsAndConditions()
                .onSuccess { regulations ->
                    updateState {
                        copy(
                            isLoading = false,
                            isError = false,
                            regulations = regulations
                        )
                    }
                }
                .onFailure {
                    updateState {
                        copy(
                            isLoading = false,
                            isError = true,
                            regulations = emptyList()
                        )
                    }
                }
        }
    }

    fun onBack() {
        viewModelScope.launch {
            updateState { copy(navigateUp = triggered) }
        }
    }

    fun onNavigatedUp() {
        viewModelScope.launch {
            updateState { copy(navigateUp = consumed) }
        }
    }
}

internal data class TermsAndConditionsUiState(
    val navigateUp: StateEvent,
    val isLoading: Boolean,
    val isError: Boolean,
    val regulations: List<RegulationPoint>
) {
    companion object {
        val DEFAULT = TermsAndConditionsUiState(
            navigateUp = consumed,
            isLoading = false,
            isError = false,
            regulations = emptyList()
        )
    }
}
