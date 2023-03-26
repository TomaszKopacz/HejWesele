package com.hejwesele.login

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateViewModel
import com.hejwesele.android.osinfo.OsInfo
import com.hejwesele.android.theme.Label
import com.hejwesele.encryption.base64
import com.hejwesele.encryption.bytes
import com.hejwesele.encryption.sha256
import com.hejwesele.encryption.string
import com.hejwesele.events.model.Event
import com.hejwesele.events.model.EventSettings
import com.hejwesele.login.usecase.FindEvent
import com.hejwesele.login.usecase.StoreEvent
import com.hejwesele.validation.StringNotEmpty
import com.hejwesele.validation.ValidationResult.Invalid
import com.hejwesele.validation.ValidationResult.Valid
import com.hejwesele.validation.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val osInfo: OsInfo,
    private val findEvent: FindEvent,
    private val storeEvent: StoreEvent
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
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            delay(LOGIN_DELAY)

            findEvent(state.eventNameInput)
                .onSuccess { event -> handleEvent(event) }
                .onFailure { updateState { copy(isLoading = false, isError = true) } }
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

    private suspend fun handleEvent(event: Event?) {
        validateEvent(
            event = event,
            onFound = {
                validatePassword(
                    inputPassword = state.eventPasswordInput,
                    eventPassword = it.password,
                    onValid = { storeEventOnDevice(it) },
                    onInvalid = {
                        updateState {
                            copy(
                                isLoading = false,
                                isError = false,
                                eventNameError = null,
                                eventPasswordError = Throwable(Label.loginIncorrectPasswordErrorLabel)
                            )
                        }
                    }
                )
            },
            onNotFound = {
                updateState {
                    copy(
                        isLoading = false,
                        isError = false,
                        eventNameError = Throwable(Label.loginEventNotFoundErrorLabel),
                        eventPasswordError = null
                    )
                }
            }
        )
    }

    private suspend fun validateEvent(
        event: Event?,
        onFound: suspend (Event) -> Unit,
        onNotFound: suspend () -> Unit
    ) {
        if (event != null) onFound(event) else onNotFound()
    }

    private suspend fun validatePassword(
        inputPassword: String,
        eventPassword: String,
        onValid: suspend () -> Unit,
        onInvalid: suspend () -> Unit
    ) = withContext(Dispatchers.Default) {
        if (inputPassword.bytes().sha256().base64(osInfo).string() == eventPassword) {
            onValid()
        } else {
            onInvalid()
        }
    }

    private suspend fun storeEventOnDevice(event: Event) = withContext(Dispatchers.IO) {
        storeEvent(event.toEventSettings())
            .onSuccess {
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
            .onFailure {
                updateState {
                    copy(
                        isLoading = false,
                        isError = true
                    )
                }
            }
    }

    private fun Event.toEventSettings() = EventSettings(
        id = id,
        name = name,
        date = date,
        invitationId = invitationId,
        galleryId = galleryId,
        galleryHintDismissed = false
    )

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
            isLoading = false,
            isError = false,
            eventNameError = null,
            eventPasswordError = null,
            isFormValid = false
        )
    }
}
