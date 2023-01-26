package com.hejwesele.usecase.event

import com.hejwesele.repository.IEventRepository
import javax.inject.Inject

class GetEvent @Inject constructor(
    private val repository: IEventRepository
) {

    suspend operator fun invoke(eventId: String) =
        repository.getEvent(eventId)
}
