package com.hejwesele.login

import android.Manifest
import androidx.lifecycle.viewModelScope
import com.hejwesele.android.components.AlertData
import com.hejwesele.android.mvvm.StateEventsViewModel
import com.hejwesele.android.theme.Label
import com.hejwesele.encryption.base64
import com.hejwesele.encryption.bytes
import com.hejwesele.encryption.sha256
import com.hejwesele.encryption.string
import com.hejwesele.permissions.PermissionsHandler
import com.hejwesele.usecase.LogIn
import com.hejwesele.validation.CheckboxChecked
import com.hejwesele.validation.StringNotEmpty
import com.hejwesele.validation.ValidationResult.Invalid
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
    private val permissionsHandler: PermissionsHandler,
    private val logIn: LogIn
) : StateEventsViewModel<LoginUiState, LoginUiEvents>(LoginUiState.Default, LoginUiEvents.Default) {

    private var state = State()

    private val eventNameValidator = Validator(
        StringNotEmpty(error = Label.loginEventNameEmptyErrorText)
    )

    private val eventPasswordValidator = Validator(
        StringNotEmpty(error = Label.loginEventPasswordEmptyErrorText)
    )

    private val termsAndConditionsValidator = Validator(
        CheckboxChecked(error = Label.loginTermsAndConditionsNotAcceptedErrorText)
    )

    fun onNameInputChanged(text: String) {
        state = state.copy(eventNameInput = text)

        validateInput(
            input = text,
            validator = eventNameValidator,
            onValid = {
                updateState {
                    copy(
                        eventNameInput = text,
                        eventNameError = null,
                        isFormValid = isFormValid()
                    )
                }
            },
            onInvalid = { error ->
                updateState {
                    copy(
                        eventNameInput = text,
                        eventNameError = Throwable(error),
                        isFormValid = false
                    )
                }
            }
        )
    }

    fun onPasswordInputChanged(text: String) {
        state = state.copy(eventPasswordInput = text)
        validateInput(
            input = text,
            validator = eventPasswordValidator,
            onValid = {
                updateState {
                    copy(
                        eventPasswordInput = text,
                        eventPasswordError = null,
                        isFormValid = isFormValid()
                    )
                }
            },
            onInvalid = { error ->
                updateState {
                    copy(
                        eventPasswordInput = text,
                        eventPasswordError = Throwable(error),
                        isFormValid = false
                    )
                }
            }
        )
    }

    fun onInformationRequested() {
        viewModelScope.launch {
            updateEvents { copy(openInformation = triggered) }
        }
    }

    fun onHelpRequested() {
        viewModelScope.launch {
            updateEvents { copy(showHelp = triggered) }
        }
    }

    fun onTermsAndConditionsCheckedChanged(checked: Boolean) {
        state = state.copy(termsAndConditionsAccepted = checked)
        viewModelScope.launch {
            updateState {
                copy(termsAndConditionsAccepted = checked, isFormValid = isFormValid())
            }
        }
    }

    fun onTermsAndConditionsRequested() {
        viewModelScope.launch {
            updateEvents { copy(openTermsAndConditions = triggered) }
        }
    }

    fun onSubmit() {
        viewModelScope.launch(Dispatchers.IO) {
            updateState { copy(isLoading = true) }

            delay(LOGIN_DELAY)

            logIn(
                name = state.eventNameInput,
                password = state.eventPasswordInput.encodePassword(),
                onServiceError = {
                    sendServiceError()
                },
                onEventNotFound = {
                    sendEventNameError()
                },
                onPasswordInvalid = {
                    sendEventPasswordError()
                },
                onSaveEventFailed = {
                    sendServiceError()
                },
                onDone = {
                    sendSuccess()
                }
            )
        }
    }

    fun onScanQrClicked() {
        viewModelScope.launch {
            val permission = Manifest.permission.CAMERA
            val permissionsGranted = permissionsHandler.checkPermission(permission)

            if (permissionsGranted) {
                updateEvents { copy(openQrScanner = triggered) }
            } else {
                updateEvents { copy(requestCameraPermission = triggered(permission)) }
            }
        }
    }

    fun onCameraPermissionResult(isGranted: Boolean) {
        if (isGranted) {
            updateEvents { copy(openQrScanner = triggered) }
        }
    }

    fun onInformationOpened() {
        updateEvents { copy(openInformation = consumed) }
    }

    fun onHelpShown() {
        updateEvents { copy(showHelp = consumed) }
    }

    fun onTermsAndConditionsOpened() {
        updateEvents { copy(openTermsAndConditions = consumed) }
    }

    fun onEventOpened() {
        viewModelScope.launch {
            updateEvents { copy(openEvent = consumed) }
        }
    }

    fun onCameraPermissionRequested() {
        viewModelScope.launch {
            updateEvents { copy(requestCameraPermission = consumed()) }
        }
    }

    fun onQrScannerOpened() {
        viewModelScope.launch {
            updateEvents { copy(openQrScanner = consumed) }
        }
    }

    private fun isFormValid(): Boolean = with(state) {
        val nameInputResult = eventNameValidator.validate(eventNameInput)
        val passwordInputResult = eventPasswordValidator.validate(eventPasswordInput)
        val termsAndConditionsResult = termsAndConditionsValidator.validate(termsAndConditionsAccepted)

        (nameInputResult + passwordInputResult + termsAndConditionsResult).isValid
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

    private fun sendServiceError() {
        updateState {
            copy(
                isLoading = false,
                loginError = AlertData.Default.copy(
                    onDismiss = ::onAlertDismissed
                )
            )
        }
    }

    private fun sendEventNameError() {
        updateState {
            copy(
                isLoading = false,
                eventNameError = Throwable(Label.loginEventNotFoundErrorText),
                eventPasswordError = null,
                loginError = null
            )
        }
    }

    private fun sendEventPasswordError() {
        updateState {
            copy(
                isLoading = false,
                eventNameError = null,
                eventPasswordError = Throwable(Label.loginIncorrectPasswordErrorText),
                loginError = null
            )
        }
    }

    private fun sendSuccess() {
        updateState {
            copy(
                isLoading = false,
                eventNameError = null,
                eventPasswordError = null,
                loginError = null
            )
        }
        updateEvents { copy(openEvent = triggered) }
    }

    private fun onAlertDismissed() {
        viewModelScope.launch {
            updateState { copy(loginError = null) }
        }
    }

    private fun String.encodePassword() = bytes().sha256().base64().string()

    private data class State(
        val eventNameInput: String = "",
        val eventPasswordInput: String = "",
        val termsAndConditionsAccepted: Boolean = false
    )

    companion object {
        private const val LOGIN_DELAY = 1_000L
    }
}

internal data class LoginUiState(
    val isLoading: Boolean,
    val eventNameInput: String,
    val eventPasswordInput: String,
    val eventNameError: Throwable?,
    val eventPasswordError: Throwable?,
    val termsAndConditionsAccepted: Boolean,
    val isFormValid: Boolean,
    val loginError: AlertData?
) {

    companion object {
        val Default = LoginUiState(
            isLoading = false,
            eventNameInput = "",
            eventPasswordInput = "",
            eventNameError = null,
            eventPasswordError = null,
            termsAndConditionsAccepted = false,
            isFormValid = false,
            loginError = null
        )
    }
}

internal data class LoginUiEvents(
    val openInformation: StateEvent,
    val showHelp: StateEvent,
    val openTermsAndConditions: StateEvent,
    val openEvent: StateEvent,
    val requestCameraPermission: StateEventWithContent<String>,
    val openQrScanner: StateEvent
) {

    companion object {
        val Default = LoginUiEvents(
            openInformation = consumed,
            showHelp = consumed,
            openTermsAndConditions = consumed,
            openEvent = consumed,
            requestCameraPermission = consumed(),
            openQrScanner = consumed
        )
    }
}
