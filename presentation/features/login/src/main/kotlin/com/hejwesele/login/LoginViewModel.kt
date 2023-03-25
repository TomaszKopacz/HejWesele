package com.hejwesele.login

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateViewModel
import com.hejwesele.android.osinfo.OsInfo
import com.hejwesele.android.theme.Label
import com.hejwesele.encryption.base64
import com.hejwesele.encryption.bytes
import com.hejwesele.encryption.fromBase64
import com.hejwesele.encryption.sha256
import com.hejwesele.encryption.string
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val osInfo: OsInfo
) : StateViewModel<LoginUiState>(LoginUiState.DEFAULT) {

    private var state = State()

    fun onNameInputChanged(text: String) {
        state = state.copy(eventNameInput = text)
    }

    fun onPasswordInputChanged(text: String) {
        state = state.copy(eventPasswordInput = text)
    }

    fun onSubmit() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            val eventPassword = getEventPassword().bytes().fromBase64(osInfo).string()
            val inputPassword = state.eventPasswordInput.bytes().sha256().string()

            if (eventPassword == inputPassword) {
                updateState { copy(isLoading = false, openEvent = triggered) }
            } else {
                updateState { copy(isLoading = false, eventPasswordError = Throwable(Label.loginIncorrectPasswordLabel)) }
            }
        }
    }

    fun onEventOpened() {
        viewModelScope.launch {
            updateState { copy(openEvent = triggered) }
        }
    }

    private fun getEventPassword(): String {
        return "DUPA".bytes().sha256().base64(osInfo).string()
    }

    private data class State(
        val eventNameInput: String = "",
        val eventPasswordInput: String = ""
    )
}

internal data class LoginUiState(
    val openEvent: StateEvent,
    val isLoading: Boolean,
    val eventNameError: Throwable?,
    val eventPasswordError: Throwable?
) {
    companion object {
        val DEFAULT = LoginUiState(
            openEvent = consumed,
            isLoading = true,
            eventNameError = null,
            eventPasswordError = null
        )
    }
}
