package com.hejwesele.qr

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateViewModel
import com.hejwesele.usecase.LoginEvent
import com.hejwesele.usecase.ParseEventQr
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class QrScannerViewModel @Inject constructor(
    private val parseEventQr: ParseEventQr,
    private val loginEvent: LoginEvent
) : StateViewModel<QrScannerUiState>(QrScannerUiState.DEFAULT) {

    fun onBackClicked() {
        viewModelScope.launch {
            updateState { copy(navigateUp = triggered) }
        }
    }

    fun onQrScanned(text: String) {
        viewModelScope.launch(Dispatchers.Default) {
            updateState { copy(isLoading = true) }

            delay(LOGIN_DELAY)

            parseEventQr(text)
                .onSuccess { credentials ->
                    loginEvent(
                        name = credentials.name,
                        password = credentials.password,
                        onServiceError = { sendErrorState() },
                        onEventNotFound = { sendErrorState() },
                        onPasswordInvalid = { sendErrorState() },
                        onSaveEventFailed = { sendErrorState() },
                        onDone = { sendSuccessState() }
                    )
                }
                .onFailure { sendErrorState() }
        }
    }

    fun onTermsAndConditionsRequested() {
        viewModelScope.launch {
            updateState { copy(openTermsAndConditions = triggered) }
        }
    }

    fun onTermsAndConditionsPromptAccepted() {
        viewModelScope.launch {
            updateState { copy(openEvent = triggered) }
        }
    }

    fun onTermsAndConditionsPromptDeclined() {
        viewModelScope.launch {
            updateState { copy(hideTermsAndConditionsPrompt = triggered) }
        }
    }

    fun onErrorDismissed() {
        viewModelScope.launch {
            updateState { copy(isError = false) }
        }
    }

    fun onNavigatedUp() {
        viewModelScope.launch {
            updateState { copy(navigateUp = consumed) }
        }
    }

    fun onTermsAndConditionsOpened() {
        viewModelScope.launch {
            updateState { copy(openTermsAndConditions = consumed) }
        }
    }

    fun onTermsAndConditionsPromptShown() {
        viewModelScope.launch {
            updateState { copy(showTermsAndConditionsPrompt = consumed) }
        }
    }

    fun onTermsAndConditionsPromptHidden() {
        viewModelScope.launch {
            updateState { copy(hideTermsAndConditionsPrompt = consumed) }
        }
    }

    fun onEventOpened() {
        viewModelScope.launch {
            updateState { copy(openEvent = consumed) }
        }
    }

    private fun sendErrorState() {
        updateState {
            copy(
                isLoading = false,
                isError = true
            )
        }
    }

    private fun sendSuccessState() {
        updateState {
            copy(
                showTermsAndConditionsPrompt = triggered,
                isLoading = false,
                isError = false
            )
        }
    }

    companion object {
        private const val LOGIN_DELAY = 1_000L
    }
}

internal data class QrScannerUiState(
    val navigateUp: StateEvent,
    val openTermsAndConditions: StateEvent,
    val showTermsAndConditionsPrompt: StateEvent,
    val hideTermsAndConditionsPrompt: StateEvent,
    val openEvent: StateEvent,
    val isLoading: Boolean,
    val isError: Boolean
) {
    companion object {
        val DEFAULT = QrScannerUiState(
            navigateUp = consumed,
            openTermsAndConditions = consumed,
            showTermsAndConditionsPrompt = consumed,
            hideTermsAndConditionsPrompt = consumed,
            openEvent = consumed,
            isLoading = false,
            isError = false
        )
    }
}
