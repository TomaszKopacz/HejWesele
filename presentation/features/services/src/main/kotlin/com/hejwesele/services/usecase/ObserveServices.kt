package com.hejwesele.services.usecase

import com.hejwesele.services.ServicesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ObserveServices @Inject constructor(
    private val repository: ServicesRepository
) {

    suspend operator fun invoke(servicesId: String) = withContext(Dispatchers.IO) {
        repository.observeServices(servicesId)
    }
}
