package com.hejwesele.gallery.usecase

import com.hejwesele.details.DetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ObserveDetails @Inject constructor(
    private val repository: DetailsRepository
) {

    suspend operator fun invoke(detailsId: String) = withContext(Dispatchers.IO) {
        repository.observeDetails(detailsId)
    }
}
