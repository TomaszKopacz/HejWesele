package com.hejwesele.services.usecase

import android.content.res.Resources.NotFoundException
import com.hejwesele.services.ServicesRepository
import com.hejwesele.services.model.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetService @Inject constructor(
    private val getEventSettings: GetEventSettings,
    private val repository: ServicesRepository
) {

    suspend operator fun invoke(serviceId: String): Result<Service> = withContext(Dispatchers.IO) {
        getEventSettings()
            .mapCatching { event ->
                val servicesId = event.servicesId
                requireNotNull(servicesId) { "Required event services are not present." }

                val services = repository.getServices(servicesId).getOrThrow()
                val service = (services.partners + services.attractions).find { service ->
                    service.id == serviceId
                }

                service ?: throw NotFoundException("Required service with id $serviceId not found.")
            }
    }
}
