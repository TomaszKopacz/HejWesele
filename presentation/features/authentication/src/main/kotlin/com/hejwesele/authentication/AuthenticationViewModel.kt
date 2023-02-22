package com.hejwesele.authentication

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateActionsViewModel
import com.hejwesele.authentication.AuthenticationUiAction.ShowLoggingInMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

internal data class AuthenticationUiState(
    val isAuthenticating: Boolean = false
)

internal sealed class AuthenticationUiAction {
    object ShowLoggingInMessage : AuthenticationUiAction()
}

@HiltViewModel
internal class AuthenticationViewModel @Inject constructor(
    private val navigator: AuthenticationNavigator
) : StateActionsViewModel<AuthenticationUiState, AuthenticationUiAction>(AuthenticationUiState()) {

    @Suppress("MagicNumber")
    fun authenticate() = viewModelScope.launch {
        updateState { copy(isAuthenticating = true) }
        emitAction(ShowLoggingInMessage)
        delay(5000L)
        navigator.openDashboard()
    }

    fun configure() = viewModelScope.launch {
        navigator.openConfiguration()
    }
}
