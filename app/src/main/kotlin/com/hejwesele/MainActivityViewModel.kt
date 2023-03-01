package com.hejwesele

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.hejwesele.MainActivityUiState.Companion.DEFAULT
import com.hejwesele.android.mvvm.StateViewModel
import com.hejwesele.navigation.MainNavGraph
import com.hejwesele.result.onError
import com.hejwesele.result.onSuccess
import com.hejwesele.settings.SettingsRepository
import com.ramcosta.composedestinations.spec.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

internal data class MainActivityUiState(
    val startRoute: Route?
) {
    companion object {
        val DEFAULT = MainActivityUiState(startRoute = null)
    }
}

@HiltViewModel
internal class MainActivityViewModel @Inject constructor(
    private val repository: SettingsRepository // TODO - create use case
) : StateViewModel<MainActivityUiState>(DEFAULT) {

    init {
        viewModelScope.launch {
            repository.getStoredSettings()
                .onSuccess { settings ->
                    val startRoute = if (settings.event != null) {
                        MainNavGraph
                    } else {
                        // TODO - return login route
                        MainNavGraph
                    }
                    updateState { copy(startRoute = startRoute) }
                }
                .onError {
                    // TODO - return login route
                    updateState { copy(startRoute = MainNavGraph) }
                }
        }
    }
}
