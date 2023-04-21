package com.hejwesele.home.home.usecase

import com.hejwesele.events.EventsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Logout @Inject constructor(
    private val eventsRepository: EventsRepository
) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        eventsRepository.removeStoredEventSettings()
    }
}
