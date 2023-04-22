package com.hejwesele.cms

import com.hejwesele.details.DetailsRepository
import com.hejwesele.details.model.Details
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

class AddDetails @Inject constructor(
    private val detailsRepository: DetailsRepository
) {
    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        detailsRepository.addDetails(
            Details(date = "2022-07-14T15:30".toLocalDateTime())
        )
    }
}
