package com.hejwesele.services

import com.hejwesele.remotedatabase.RemoteDatabase
import com.hejwesele.result.notFound
import com.hejwesele.services.dto.ServicesDto
import com.hejwesele.services.mappers.mapModel
import com.hejwesele.services.model.Services
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDatabaseServicesRemoteSource @Inject constructor(
    private val database: RemoteDatabase
) : ServicesRemoteSource {

    companion object {
        private const val SERVICES_PATH = "services/"
    }

    override suspend fun observeServices(servicesId: String): Flow<Result<Services>> = withContext(Dispatchers.IO) {
        database.observe(
            path = SERVICES_PATH,
            id = servicesId,
            type = ServicesDto::class
        ).map { result ->
            result.mapCatching { dto -> dto.mapModel() }
        }
    }

    override suspend fun getServices(servicesId: String): Result<Services> = withContext(Dispatchers.IO) {
        database.read(
            path = SERVICES_PATH,
            id = servicesId,
            type = ServicesDto::class
        ).mapCatching { dto ->
            dto?.mapModel() ?: throw notFound(name = "services", id = servicesId)
        }
    }
}
