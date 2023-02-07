package com.hejwesele.home.usecase

import com.hejwesele.events.EventsRepository
import javax.inject.Inject

class GetEvents @Inject constructor(
    private val repository: EventsRepository
) {

    suspend operator fun invoke() = repository.getEvents()
}
