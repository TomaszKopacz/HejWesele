package com.hejwesele.navigation

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateViewModel
import com.hejwesele.navigation.AppNavigationUiState.Companion.DEFAULT
import com.hejwesele.navigation.usecase.IsLoggedIn
import com.ramcosta.composedestinations.spec.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

internal data class AppNavigationUiState(
    val startRoute: Route?
) {
    companion object {
        val DEFAULT = AppNavigationUiState(startRoute = null)
    }
}

@HiltViewModel
internal class AppNavigationViewModel @Inject constructor(
    private val isLoggedIn: IsLoggedIn
) : StateViewModel<AppNavigationUiState>(DEFAULT) {

    init {
        viewModelScope.launch {
            val startRoute = if (isLoggedIn()) MainNavGraph else LoginNavGraph
            updateState { copy(startRoute = startRoute) }
        }
    }
}
