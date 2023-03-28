package com.hejwesele.qr

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateViewModel
import com.hejwesele.qr.usecase.ParseEventQr
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
    private val parseEventQr: ParseEventQr
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
                    if (credentials.name == "hej" && credentials.password == "nEeL9j6VAMtdsehezoLxjI655S4vkTWs1/EJcsjVY7o=") {
                        updateState { copy(isLoading = false, openEvent = triggered) }
                    } else {
                        // TODO - handle error
                        updateState { copy(isLoading = false) }
                    }
                }
                .onFailure {
                    // TODO - handle error
                    updateState { copy(isLoading = false) }
                }
        }
    }

    fun onNavigatedUp() {
        viewModelScope.launch {
            updateState { copy(navigateUp = consumed) }
        }
    }

    fun onEventOpened() {
        viewModelScope.launch {
            updateState { copy(openEvent = consumed) }
        }
    }

    companion object {
        private const val LOGIN_DELAY = 1_000L
    }
}

internal data class QrScannerUiState(
    val navigateUp: StateEvent,
    val openEvent: StateEvent,
    val isLoading: Boolean
) {
    companion object {
        val DEFAULT = QrScannerUiState(
            navigateUp = consumed,
            openEvent = consumed,
            isLoading = false
        )
    }
}
