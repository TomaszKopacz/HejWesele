package com.hejwesele.services

import com.hejwesele.services.model.Services
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServicesRepository @Inject constructor(
    private val remoteSource: ServicesRemoteSource
) {

    suspend fun observeServices(servicesId: String) = withContext(Dispatchers.IO) {
        remoteSource.observeServices(servicesId)
    }

    suspend fun getServices(servicesId: String) = withContext(Dispatchers.IO) {
        remoteSource.getServices(servicesId)
    }

    suspend fun addServices(services: Services) = withContext(Dispatchers.IO) {
        remoteSource.addServices(services)
    }
}
