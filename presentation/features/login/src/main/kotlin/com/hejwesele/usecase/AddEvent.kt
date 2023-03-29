package com.hejwesele.usecase

import com.hejwesele.encryption.base64
import com.hejwesele.encryption.bytes
import com.hejwesele.encryption.sha256
import com.hejwesele.encryption.string
import com.hejwesele.events.EventsRepository
import com.hejwesele.events.model.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

// TODO - this use case is only for development, should be removed from here
internal class AddEvent @Inject constructor(
    private val repository: EventsRepository
) {

    suspend operator fun invoke(event: Event) = withContext(Dispatchers.IO) {
        repository.addEvent(
            Event(
                id = "",
                name = "hej",
                password = "hej".bytes().sha256().base64().string(),
                date = "2024-07-14T15:30:00".toLocalDateTime(),
                invitationId = "-NNxXTrIHZwNNP1ZUlWP",
                galleryId = null
            )
        )
    }
}
