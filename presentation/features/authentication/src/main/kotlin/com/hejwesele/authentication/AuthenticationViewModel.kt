package com.hejwesele.authentication

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateActionsViewModel
import com.hejwesele.authentication.AuthenticationUiAction.ShowLoggingInMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthenticationUiState(
    val isAuthenticating: Boolean = false
)

sealed class AuthenticationUiAction {
    object ShowLoggingInMessage : AuthenticationUiAction()
}

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    // private val navigator: Navigator
) : StateActionsViewModel<AuthenticationUiState, AuthenticationUiAction>(AuthenticationUiState()) {

    @Suppress("MagicNumber")
    fun authenticate() = viewModelScope.launch {
        updateState { copy(isAuthenticating = true) }
        emitAction(ShowLoggingInMessage)
        delay(5000L)
        // navigator.navigate(Destinations.main(userId = 44))
    }
}
