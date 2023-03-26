package com.hejwesele.login.usecase

import com.hejwesele.events.EventsRepository
import com.hejwesele.events.model.EventSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class StoreEvent @Inject constructor(
    private val repository: EventsRepository
) {
    suspend operator fun invoke(eventSettings: EventSettings) = withContext(Dispatchers.IO) {
        repository.storeEventSettings(eventSettings)
    }
}
