package com.hejwesele.services

import com.hejwesele.services.model.Services
import kotlinx.coroutines.flow.Flow

interface ServicesRemoteSource {

    suspend fun observeServices(servicesId: String): Flow<Result<Services>>

    suspend fun getServices(servicesId: String): Result<Services>

    suspend fun addServices(services: Services): Result<Services>
}
