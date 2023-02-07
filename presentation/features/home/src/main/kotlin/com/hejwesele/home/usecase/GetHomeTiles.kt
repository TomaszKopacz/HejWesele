package com.hejwesele.home.usecase

import com.hejwesele.events.EventsRepository
import com.hejwesele.result.mapSuccess
import javax.inject.Inject

class GetHomeTiles @Inject constructor(
    private val repository: EventsRepository
) {

    suspend operator fun invoke(eventId: String) =
        repository.getEvent(eventId).mapSuccess { it.homeTiles }
}
