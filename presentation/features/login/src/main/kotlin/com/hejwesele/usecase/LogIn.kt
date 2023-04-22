package com.hejwesele.usecase

import com.hejwesele.events.model.Event
import com.hejwesele.events.model.EventSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class LogIn @Inject constructor(
    private val findEvent: FindEvent,
    private val storeEvent: StoreEvent
) {

    suspend operator fun invoke(
        name: String,
        password: String,
        onServiceError: (Throwable) -> Unit,
        onEventNotFound: () -> Unit,
        onPasswordInvalid: () -> Unit,
        onSaveEventFailed: () -> Unit,
        onDone: () -> Unit
    ) = withContext(Dispatchers.IO) {
        findEvent(name)
            .onSuccess { event ->
                handleEvent(
                    event = event,
                    password = password,
                    onEventNotFound = onEventNotFound,
                    onPasswordInvalid = onPasswordInvalid,
                    onSaveEventFailed = onSaveEventFailed,
                    onDone = onDone
                )
            }
            .onFailure { onServiceError(it) }
    }

    private suspend fun handleEvent(
        event: Event?,
        password: String,
        onEventNotFound: () -> Unit,
        onPasswordInvalid: () -> Unit,
        onSaveEventFailed: () -> Unit,
        onDone: () -> Unit
    ) {
        validateEvent(
            event = event,
            onFound = {
                validatePassword(
                    givenPassword = password,
                    eventPassword = it.password,
                    onValid = {
                        saveEventOnDevice(
                            event = it,
                            onDone = onDone,
                            onFailed = onSaveEventFailed
                        )
                    },
                    onInvalid = { onPasswordInvalid() }
                )
            },
            onEventNotFound = onEventNotFound
        )
    }

    private suspend fun validateEvent(
        event: Event?,
        onFound: suspend (Event) -> Unit,
        onEventNotFound: suspend () -> Unit
    ) {
        if (event != null) onFound(event) else onEventNotFound()
    }

    private suspend fun validatePassword(
        givenPassword: String,
        eventPassword: String,
        onValid: suspend () -> Unit,
        onInvalid: suspend () -> Unit
    ) = withContext(Dispatchers.Default) {
        if (givenPassword == eventPassword) {
            onValid()
        } else {
            onInvalid()
        }
    }

    private suspend fun saveEventOnDevice(
        event: Event,
        onDone: () -> Unit,
        onFailed: () -> Unit
    ) = withContext(Dispatchers.IO) {
        storeEvent(event.toEventSettings())
            .onSuccess { onDone() }
            .onFailure { onFailed() }
    }

    private fun Event.toEventSettings() = EventSettings(
        id = id,
        name = name,
        detailsId = detailsId,
        invitationId = invitationId,
        galleryId = galleryId,
        galleryHintDismissed = false
    )
}
