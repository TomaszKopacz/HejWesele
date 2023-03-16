package com.hejwesele.settings

import com.hejwesele.protodatastore.ProtoDataStore
import com.hejwesele.protodatastore.ProtoMessageSpecification
import com.hejwesele.settings.di.SettingsProtoDataStore
import com.hejwesele.settings.dto.EventSettingsDto
import com.hejwesele.settings.mappers.mapDto
import com.hejwesele.settings.mappers.mapModel
import com.hejwesele.settings.model.Event
import com.hejwesele.settings.model.EventSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProtoDataStoreSettingsLocalSource @Inject constructor(
    @SettingsProtoDataStore private val datastore: ProtoDataStore<EventSettingsDto>
) : SettingsLocalSource {

    override suspend fun getStoredSettings(): Result<EventSettings> = withContext(Dispatchers.IO) {
        datastore.readData().mapCatching { it.mapModel() }
    }

    override suspend fun setEvent(event: Event): Result<Event> = withContext(Dispatchers.IO) {
        datastore.writeData { it.copy(event = event.mapDto()) }.map { event }
    }

    override suspend fun setGalleryHintDismissed(dismissed: Boolean): Result<Boolean> = withContext(Dispatchers.IO) {
        datastore.writeData { it.copy(galleryHintDismissed = dismissed) }.map { dismissed }
    }

    companion object {
        const val SETTINGS_DATASTORE_FILE = "settings"

        val settingsSpecification = ProtoMessageSpecification(
            initialValue = EventSettingsDto(),
            reader = { input ->
                Json.decodeFromString(
                    deserializer = EventSettingsDto.serializer(),
                    string = input.readBytes().decodeToString()
                )
            },
            writer = { item, output ->
                output.write(
                    Json
                        .encodeToString(EventSettingsDto.serializer(), item)
                        .encodeToByteArray()
                )
            }
        )
    }
}
