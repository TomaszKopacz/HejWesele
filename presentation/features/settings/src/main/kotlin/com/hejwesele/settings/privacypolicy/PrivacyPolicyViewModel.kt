package com.hejwesele.settings.privacypolicy

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateViewModel
import com.hejwesele.regulations.model.RegulationPoint
import com.hejwesele.settings.usecase.GetPrivacyPolicy
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
) : StateViewModel<PrivacyPolicyUiState>(PrivacyPolicyUiState.DEFAULT) {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            updateState { copy(isLoading = true) }

            getPrivacyPolicy()
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

internal data class PrivacyPolicyUiState(
    val navigateUp: StateEvent,
    val isLoading: Boolean,
    val isError: Boolean,
    val regulations: List<RegulationPoint>
) {
    companion object {
        val DEFAULT = PrivacyPolicyUiState(
            navigateUp = consumed,
            isLoading = false,
            isError = false,
            regulations = emptyList()
        )
    }
}
