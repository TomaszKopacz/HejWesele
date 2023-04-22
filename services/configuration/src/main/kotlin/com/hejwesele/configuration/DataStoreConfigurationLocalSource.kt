package com.hejwesele.configuration

import com.hejwesele.datastore.DataStore
import com.hejwesele.datastore.MessageSpecification
import com.hejwesele.configuration.di.ConfigurationDataStore
import com.hejwesele.configuration.dto.ConfigurationDto
import com.hejwesele.configuration.mappers.mapDto
import com.hejwesele.configuration.mappers.mapModel
import com.hejwesele.configuration.model.Configuration
import com.hejwesele.configuration.model.Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreConfigurationLocalSource @Inject constructor(
    @ConfigurationDataStore private val configurationStore: DataStore<ConfigurationDto>
) : ConfigurationLocalSource {

    override suspend fun getConfiguration(): Result<Configuration> = withContext(Dispatchers.IO) {
        configurationStore.readData().mapCatching { it.mapModel() }
    }

    override fun observeConfiguration(): Flow<Configuration> =
        configurationStore.observeData().map { it.mapModel() }.flowOn(Dispatchers.IO)

    override suspend fun setConfiguration(configuration: Configuration): Result<Configuration> = withContext(Dispatchers.IO) {
        configurationStore.writeData { configuration.mapDto() }.map { configuration }
    }

    override suspend fun setAppTheme(theme: Theme): Result<Theme> = withContext(Dispatchers.IO) {
        configurationStore.writeData { it.copy(theme = theme.mapDto()) }.map { theme }
    }

    companion object {
        const val CONFIGURATION_DATASTORE_FILE = "configuration"

        val configurationSpecification = MessageSpecification(
            initialValue = ConfigurationDto(),
            reader = { input ->
                Json.decodeFromString(
                    deserializer = ConfigurationDto.serializer(),
                    string = input.readBytes().decodeToString()
                )
            },
            writer = { item, output ->
                output.write(
                    Json
                        .encodeToString(ConfigurationDto.serializer(), item)
                        .encodeToByteArray()
                )
            }
        )
    }
}
