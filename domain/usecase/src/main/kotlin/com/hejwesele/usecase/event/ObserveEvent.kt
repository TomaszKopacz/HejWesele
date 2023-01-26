package com.hejwesele.usecase.event

import com.hejwesele.repository.IEventRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class ObserveEvent @Inject constructor(
    private val repository: IEventRepository
) {

    suspend operator fun invoke(eventId: String, coroutineScope: CoroutineScope) =
        repository.observeEvent(eventId, coroutineScope)
}
