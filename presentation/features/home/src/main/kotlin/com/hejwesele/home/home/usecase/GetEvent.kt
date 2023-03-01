package com.hejwesele.home.home.usecase

import com.hejwesele.events.EventsRepository
import javax.inject.Inject

internal class GetEvent @Inject constructor(
    private val repository: EventsRepository
) {

    suspend operator fun invoke(eventId: String) = repository.getEvent("-NNxhUpPm4DzrEhKItsY")
}
