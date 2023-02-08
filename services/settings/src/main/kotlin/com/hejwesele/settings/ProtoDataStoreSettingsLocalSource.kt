package com.hejwesele.settings

import com.hejwesele.protodatastore.ProtoDataStore
import com.hejwesele.protodatastore.ProtoMessageSpecification
import com.hejwesele.result.CompletableResult
import com.hejwesele.result.Result
import com.hejwesele.result.flatMapSuccess
import com.hejwesele.settings.di.SettingsProtoDataStore
import com.hejwesele.settings.dto.EventSettingsDto
import com.hejwesele.settings.mappers.mapDto
import com.hejwesele.settings.mappers.safeMapModel
import com.hejwesele.settings.model.Event
import com.hejwesele.settings.model.EventSettings
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ProtoDataStoreSettingsLocalSource @Inject constructor(
    @SettingsProtoDataStore private val datastore: ProtoDataStore<EventSettingsDto>
): SettingsLocalSource {

    override suspend fun getStoredSettings(): Result<EventSettings> {
        return datastore.readData()
            .flatMapSuccess {
                it.safeMapModel()
            }
    }

    override suspend fun setEvent(event: Event): CompletableResult {
        return datastore.writeData {
            it.copy(
                event = event.mapDto()
            )
        }
    }

    override suspend fun setGalleryHintDismissed(dismissed: Boolean): CompletableResult {
        return datastore.writeData {
            it.copy(
                galleryHintDismissed = dismissed
            )
        }
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
