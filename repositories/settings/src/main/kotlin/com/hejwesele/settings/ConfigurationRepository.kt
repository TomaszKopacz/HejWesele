package com.hejwesele.settings

import com.hejwesele.settings.model.Configuration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigurationRepository @Inject constructor(
    private val localSource: ConfigurationLocalSource
) {

    suspend fun storeConfiguration(configuration: Configuration): Result<Configuration> = withContext(Dispatchers.IO) {
        localSource.setConfiguration(configuration)
    }

    suspend fun getStoredConfiguration(): Result<Configuration> = withContext(Dispatchers.IO) {
        localSource.getConfiguration()
    }

    fun observeStoredConfiguration(): Flow<Configuration> =
        localSource.observeConfiguration().flowOn(Dispatchers.IO)
}
