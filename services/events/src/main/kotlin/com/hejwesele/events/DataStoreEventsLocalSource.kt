package com.hejwesele.events

import com.hejwesele.datastore.DataStore
import com.hejwesele.datastore.MessageSpecification
import com.hejwesele.events.di.EventsDataStore
import com.hejwesele.events.dto.EventSettingsDto
import com.hejwesele.events.mappers.mapDto
import com.hejwesele.events.mappers.mapModel
import com.hejwesele.events.model.EventSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreEventsLocalSource @Inject constructor(
    @EventsDataStore private val eventsStore: DataStore<EventSettingsDto>
) : EventsLocalSource {

    override suspend fun getEventSettings(): Result<EventSettings> = withContext(Dispatchers.IO) {
        eventsStore.readData().mapCatching { it.mapModel() }
    }

    override suspend fun setEventSettings(eventSettings: EventSettings): Result<EventSettings> = withContext(Dispatchers.IO) {
        eventsStore.writeData { eventSettings.mapDto() }.map { eventSettings }
    }

    override suspend fun setGalleryHintDismissed(dismissed: Boolean): Result<Boolean> = withContext(Dispatchers.IO) {
        eventsStore.writeData { it.copy(galleryHintDismissed = dismissed) }.map { dismissed }
    }

    companion object {
        const val EVENTS_DATASTORE_FILE = "events"

        val eventSettingsSpecification = MessageSpecification(
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
