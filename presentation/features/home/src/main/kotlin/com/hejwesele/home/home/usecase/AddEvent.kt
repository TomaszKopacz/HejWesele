package com.hejwesele.home.home.usecase

import com.hejwesele.events.EventsRepository
import com.hejwesele.events.model.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

internal class AddEvent @Inject constructor(
    private val repository: EventsRepository
) {

    suspend operator fun invoke(event: Event) = withContext(Dispatchers.IO) {
        repository.addEvent(
            Event(
                id = "",
                name = "Tomek&Patrycja2024",
                date = "2024-07-14T15:30:00".toLocalDateTime(),
                invitationId = "-NNxXTrIHZwNNP1ZUlWP",
                galleryId = null

            )
        )
    }
}
