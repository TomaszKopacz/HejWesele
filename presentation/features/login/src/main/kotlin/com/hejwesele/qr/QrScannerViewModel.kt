package com.hejwesele.qr

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.components.DismissiveError
import com.hejwesele.android.mvvm.StateEventsViewModel
import com.hejwesele.usecase.LogIn
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
    private val logIn: LogIn
) : StateEventsViewModel<QrScannerUiState, QrScannerUiEvents>(QrScannerUiState.Default, QrScannerUiEvents.Default) {

    fun onBackClicked() {
        viewModelScope.launch {
            updateEvents { copy(navigateUp = triggered) }
        }
    }

    fun onQrScanned(text: String) {
        viewModelScope.launch(Dispatchers.Default) {
            updateState { copy(isLoading = true) }

            delay(LOGIN_DELAY)

            parseEventQr(text)
                .onSuccess { credentials ->
                    logIn(
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
            updateEvents { copy(openTermsAndConditions = triggered) }
        }
    }

    fun onTermsAndConditionsPromptAccepted() {
        viewModelScope.launch {
            updateEvents { copy(openEvent = triggered) }
        }
    }

    fun onTermsAndConditionsPromptDeclined() {
        viewModelScope.launch {
            updateEvents { copy(hideTermsAndConditionsPrompt = triggered) }
        }
    }

    fun onNavigatedUp() {
        viewModelScope.launch {
            updateEvents { copy(navigateUp = consumed) }
        }
    }

    fun onTermsAndConditionsOpened() {
        viewModelScope.launch {
            updateEvents { copy(openTermsAndConditions = consumed) }
        }
    }

    fun onTermsAndConditionsPromptShown() {
        viewModelScope.launch {
            updateEvents { copy(showTermsAndConditionsPrompt = consumed) }
        }
    }

    fun onTermsAndConditionsPromptHidden() {
        viewModelScope.launch {
            updateEvents { copy(hideTermsAndConditionsPrompt = consumed) }
        }
    }

    fun onEventOpened() {
        viewModelScope.launch {
            updateEvents { copy(openEvent = consumed) }
        }
    }

    private fun sendErrorState() {
        updateState {
            copy(
                isLoading = false,
                dismissiveError = DismissiveError.Default.copy(onDismiss = ::onErrorDismissed)
            )
        }
    }

    private fun sendSuccessState() {
        updateState { copy(isLoading = false, dismissiveError = null) }
        updateEvents { copy(showTermsAndConditionsPrompt = triggered) }
    }

    private fun onErrorDismissed() {
        viewModelScope.launch {
            updateState { copy(dismissiveError = null) }
        }
    }

    companion object {
        private const val LOGIN_DELAY = 1_000L
    }
}

internal data class QrScannerUiState(
    val isLoading: Boolean,
    val dismissiveError: DismissiveError?
) {
    companion object {
        val Default = QrScannerUiState(
            isLoading = false,
            dismissiveError = null
        )
    }
}

internal data class QrScannerUiEvents(
    val navigateUp: StateEvent,
    val openTermsAndConditions: StateEvent,
    val showTermsAndConditionsPrompt: StateEvent,
    val hideTermsAndConditionsPrompt: StateEvent,
    val openEvent: StateEvent
) {
    companion object {
        val Default = QrScannerUiEvents(
            navigateUp = consumed,
            openTermsAndConditions = consumed,
            showTermsAndConditionsPrompt = consumed,
            hideTermsAndConditionsPrompt = consumed,
            openEvent = consumed
        )
    }
}
