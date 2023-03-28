package com.hejwesele.usecase

import com.hejwesele.events.EventsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class FindEvent @Inject constructor(
    private val repository: EventsRepository
) {

    suspend operator fun invoke(eventName: String) = withContext(Dispatchers.IO) {
        repository.findEvent(eventName)
    }
}
