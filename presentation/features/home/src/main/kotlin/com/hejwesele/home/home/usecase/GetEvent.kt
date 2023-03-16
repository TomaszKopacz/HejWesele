package com.hejwesele.home.home.usecase

import com.hejwesele.events.EventsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class GetEvent @Inject constructor(
    private val repository: EventsRepository
) {

    suspend operator fun invoke(eventId: String) = withContext(Dispatchers.IO) {
        repository.getEvent("-NNxhUpPm4DzrEhKItsY")
    }
}
