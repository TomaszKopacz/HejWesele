package com.hejwesele.navigation.usecase

import com.hejwesele.events.EventsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class IsLoggedIn @Inject constructor(
    private val eventsRepository: EventsRepository
) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        eventsRepository.getStoredEventSettings().getOrNull() != null
    }
}
