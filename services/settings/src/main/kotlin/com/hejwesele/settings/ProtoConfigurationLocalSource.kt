package com.hejwesele.settings

import com.hejwesele.protodatastore.ProtoDataStore
import com.hejwesele.protodatastore.ProtoMessageSpecification
import com.hejwesele.settings.di.ConfigurationProtoDataStore
import com.hejwesele.settings.dto.ConfigurationDto
import com.hejwesele.settings.mappers.mapDto
import com.hejwesele.settings.mappers.mapModel
import com.hejwesele.settings.model.Configuration
import com.hejwesele.settings.model.Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProtoConfigurationLocalSource @Inject constructor(
    @ConfigurationProtoDataStore private val configurationStore: ProtoDataStore<ConfigurationDto>
) : ConfigurationLocalSource {

    override suspend fun getConfiguration(): Result<Configuration> = withContext(Dispatchers.IO) {
        configurationStore.readData().mapCatching { it.mapModel() }
    }

    override fun observeConfiguration(): Flow<Configuration> =
        configurationStore.observeData().map { it.mapModel() }.flowOn(Dispatchers.IO)

    override suspend fun setConfiguration(configuration: Configuration): Result<Configuration> = withContext(Dispatchers.IO) {
        configurationStore.writeData { configuration.mapDto() }.map { configuration }
    }

    override suspend fun setAppTheme(theme: Theme): Result<Theme>  = withContext(Dispatchers.IO) {
        configurationStore.writeData { it.copy(theme = theme.mapDto()) }.map { theme }
    }

    companion object {
        const val CONFIGURATION_DATASTORE_FILE = "configuration"

        val configurationSpecification = ProtoMessageSpecification(
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
