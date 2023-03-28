package com.hejwesele.login

import android.Manifest
import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateViewModel
import com.hejwesele.android.osinfo.OsInfo
import com.hejwesele.android.theme.Label
import com.hejwesele.encryption.base64
import com.hejwesele.encryption.bytes
import com.hejwesele.encryption.sha256
import com.hejwesele.encryption.string
import com.hejwesele.permissions.PermissionsHandler
import com.hejwesele.usecase.LoginEvent
import com.hejwesele.validation.StringNotEmpty
import com.hejwesele.validation.ValidationResult.Invalid
import com.hejwesele.validation.ValidationResult.Valid
import com.hejwesele.validation.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val osInfo: OsInfo,
    private val permissionsHandler: PermissionsHandler,
    private val loginEvent: LoginEvent
) : StateViewModel<LoginUiState>(LoginUiState.DEFAULT) {

    private var state = State()

    private val eventNameValidator = Validator(
        StringNotEmpty(error = Label.loginEventNameEmptyErrorLabel)
    )

    private val eventPasswordValidator = Validator(
        StringNotEmpty(error = Label.loginEventPasswordEmptyErrorLabel)
    )

    fun onNameInputChanged(text: String) {
        state = state.copy(eventNameInput = text)
        validateInput(
            input = text,
            validator = eventNameValidator,
            onValid = { updateState { copy(eventNameError = null, isFormValid = isFormValid()) } },
            onInvalid = { error -> updateState { copy(eventNameError = Throwable(error), isFormValid = false) } }
        )
    }

    fun onPasswordInputChanged(text: String) {
        state = state.copy(eventPasswordInput = text)
        validateInput(
            input = text,
            validator = eventPasswordValidator,
            onValid = { updateState { copy(eventPasswordError = null, isFormValid = isFormValid()) } },
            onInvalid = { error -> updateState { copy(eventPasswordError = Throwable(error), isFormValid = false) } }
        )
    }

    fun onHelpRequested() {
        viewModelScope.launch {
            updateState { copy(showHelp = triggered) }
        }
    }

    fun onSubmit() {
        viewModelScope.launch(Dispatchers.IO) {
            updateState { copy(isLoading = true) }

            delay(LOGIN_DELAY)

            loginEvent(
                name = state.eventNameInput,
                password = state.eventPasswordInput.encodePassword(),
                onServiceError = { sendGeneralErrorState() },
                onEventNotFound = { sendEventNotFoundState() },
                onPasswordInvalid = { sendPasswordInvalidState() },
                onSaveEventFailed = { sendEventSavingErrorState() },
                onDone = { sendSuccessState() }
            )
        }
    }

    fun onScanQrClicked() {
        viewModelScope.launch {
            val permission = Manifest.permission.CAMERA
            val permissionsGranted = permissionsHandler.checkPermission(permission)

            if (permissionsGranted) {
                updateState { copy(openQrScanner = triggered) }
            } else {
                updateState {
                    copy(requestCameraPermission = triggered(permission))
                }
            }
        }
    }

    fun onCameraPermissionResult(isGranted: Boolean) {
        if (isGranted) {
            updateState { copy(openQrScanner = triggered) }
        }
    }

    fun onErrorDismissed() {
        viewModelScope.launch {
            updateState { copy(isError = false) }
        }
    }

    fun onHelpShown() {
        updateState { copy(showHelp = consumed) }
    }

    fun onEventOpened() {
        viewModelScope.launch {
            updateState { copy(openEvent = consumed) }
        }
    }

    fun onCameraPermissionRequested() {
        viewModelScope.launch {
            updateState { copy(requestCameraPermission = consumed()) }
        }
    }

    fun onQrScannerOpened() {
        viewModelScope.launch {
            updateState { copy(openQrScanner = consumed) }
        }
    }

    private fun isFormValid(): Boolean = with(state) {
        val nameInputResult = eventNameValidator.validate(eventNameInput)
        val passwordInputResult = eventPasswordValidator.validate(eventPasswordInput)

        nameInputResult + passwordInputResult is Valid
    }

    private fun validateInput(
        input: String,
        validator: Validator<String>,
        onValid: () -> Unit,
        onInvalid: (String) -> Unit
    ) {
        with(validator.validate(input)) {
            when {
                isValid -> onValid()
                isInvalid -> onInvalid((this as Invalid).rules.first().message)
            }
        }
    }

    private fun sendGeneralErrorState() {
        updateState {
            copy(
                isLoading = false,
                isError = true
            )
        }
    }

    private fun sendEventNotFoundState() {
        updateState {
            copy(
                isLoading = false,
                isError = false,
                eventNameError = Throwable(Label.loginEventNotFoundErrorLabel),
                eventPasswordError = null
            )
        }
    }

    private fun sendPasswordInvalidState() {
        updateState {
            copy(
                isLoading = false,
                isError = false,
                eventNameError = null,
                eventPasswordError = Throwable(Label.loginIncorrectPasswordErrorLabel)
            )
        }
    }

    private fun sendEventSavingErrorState() {
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
                openEvent = triggered,
                isLoading = false,
                isError = false,
                eventNameError = null,
                eventPasswordError = null
            )
        }
    }

    private fun String.encodePassword() = bytes().sha256().base64(osInfo).string()

    private data class State(
        val eventNameInput: String = "",
        val eventPasswordInput: String = ""
    )

    companion object {
        private const val LOGIN_DELAY = 1_000L
    }
}

internal data class LoginUiState(
    val showHelp: StateEvent,
    val openEvent: StateEvent,
    val requestCameraPermission: StateEventWithContent<String>,
    val openQrScanner: StateEvent,
    val isLoading: Boolean,
    val isError: Boolean,
    val eventNameError: Throwable?,
    val eventPasswordError: Throwable?,
    val isFormValid: Boolean
) {
    companion object {
        val DEFAULT = LoginUiState(
            showHelp = consumed,
            openEvent = consumed,
            requestCameraPermission = consumed(),
            openQrScanner = consumed,
            isLoading = false,
            isError = false,
            eventNameError = null,
            eventPasswordError = null,
            isFormValid = false
        )
    }
}
