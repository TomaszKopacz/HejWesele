package com.hejwesele.usecase.home

import com.hejwesele.repository.IHomeRepository
import javax.inject.Inject

class GetHomeTiles @Inject constructor(
    private val repository: IHomeRepository
) {

    suspend operator fun invoke(eventId: String) =
        repository.getHomeTiles(eventId)
}
