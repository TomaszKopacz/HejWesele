package com.hejwesele.home.usecase

import com.hejwesele.events.EventsRepository
import javax.inject.Inject

class GetEvent @Inject constructor(
    private val repository: EventsRepository
) {

    suspend operator fun invoke(id: String) = repository.getEvent(id)
}