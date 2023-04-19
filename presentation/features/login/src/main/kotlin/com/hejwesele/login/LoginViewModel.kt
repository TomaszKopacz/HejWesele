package com.hejwesele.login

import android.Manifest
import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateViewModel
import com.hejwesele.android.theme.Label
import com.hejwesele.encryption.base64
import com.hejwesele.encryption.bytes
import com.hejwesele.encryption.sha256
import com.hejwesele.encryption.string
import com.hejwesele.permissions.PermissionsHandler
import com.hejwesele.usecase.LoginEvent
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
    private val loginEvent: LoginEvent
) : StateViewModel<LoginUiState>(LoginUiState.DEFAULT) {

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

    fun onSettingsRequested() {
        viewModelScope.launch {
            updateState { copy(openSettings = triggered) }
        }
    }

    fun onHelpRequested() {
        viewModelScope.launch {
            updateState { copy(showHelp = triggered) }
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
            updateState { copy(openTermsAndConditions = triggered) }
        }
    }

    fun onSubmit() {
        viewModelScope.launch(Dispatchers.IO) {
            updateState { copy(isLoading = true) }

            delay(LOGIN_DELAY)

            loginEvent(
                name = state.eventNameInput,
                password = state.eventPasswordInput.encodePassword(),
                onServiceError = {
                    updateState { copy(isLoading = false, isError = true) }
                },
                onEventNotFound = {
                    updateState {
                        copy(
                            isLoading = false,
                            isError = false,
                            eventNameError = Throwable(Label.loginEventNotFoundErrorText),
                            eventPasswordError = null
                        )
                    }
                },
                onPasswordInvalid = {
                    updateState {
                        copy(
                            isLoading = false,
                            isError = false,
                            eventNameError = null,
                            eventPasswordError = Throwable(Label.loginIncorrectPasswordErrorText)
                        )
                    }
                },
                onSaveEventFailed = {
                    updateState { copy(isLoading = false, isError = true) }
                },
                onDone = {
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

    fun onSettingsOpened() {
        updateState { copy(openSettings = consumed) }
    }

    fun onHelpShown() {
        updateState { copy(showHelp = consumed) }
    }

    fun onTermsAndConditionsOpened() {
        updateState { copy(openTermsAndConditions = consumed) }
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
    val openSettings: StateEvent,
    val showHelp: StateEvent,
    val openTermsAndConditions: StateEvent,
    val openEvent: StateEvent,
    val requestCameraPermission: StateEventWithContent<String>,
    val openQrScanner: StateEvent,
    val isLoading: Boolean,
    val isError: Boolean,
    val eventNameInput: String,
    val eventPasswordInput: String,
    val eventNameError: Throwable?,
    val eventPasswordError: Throwable?,
    val termsAndConditionsAccepted: Boolean,
    val isFormValid: Boolean
) {

    companion object {
        val DEFAULT = LoginUiState(
            openSettings = consumed,
            showHelp = consumed,
            openTermsAndConditions = consumed,
            openEvent = consumed,
            requestCameraPermission = consumed(),
            openQrScanner = consumed,
            isLoading = false,
            isError = false,
            eventNameInput = "",
            eventPasswordInput = "",
            eventNameError = null,
            eventPasswordError = null,
            termsAndConditionsAccepted = false,
            isFormValid = false
        )
    }
}
