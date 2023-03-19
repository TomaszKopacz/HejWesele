package com.hejwesele.navigation

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateViewModel
import com.hejwesele.events.EventsRepository
import com.hejwesele.navigation.MainActivityUiState.Companion.DEFAULT
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
internal class AppNavigationViewModel @Inject constructor(
    private val repository: EventsRepository // TODO - create use case
) : StateViewModel<MainActivityUiState>(DEFAULT) {

    init {
        viewModelScope.launch {
            repository.getStoredEvent()
                .onSuccess { settings ->
                    val startRoute = if (settings.event != null) {
                        MainNavGraph
                    } else {
                        // TODO - return login route
                        MainNavGraph
                    }
                    updateState { copy(startRoute = startRoute) }
                }
                .onFailure {
                    // TODO - return login route
                    updateState { copy(startRoute = MainNavGraph) }
                }
        }
    }
}
