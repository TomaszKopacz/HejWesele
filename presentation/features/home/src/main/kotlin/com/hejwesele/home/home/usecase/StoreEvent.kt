package com.hejwesele.home.home.usecase

import com.hejwesele.events.EventsRepository
import com.hejwesele.events.model.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class StoreEvent @Inject constructor(
    private val repository: EventsRepository
) {
    suspend operator fun invoke(event: Event) = withContext(Dispatchers.IO) {
        repository.storeEvent(event)
    }
}
