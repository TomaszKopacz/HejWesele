package com.hejwesele.events

import com.hejwesele.events.di.EventsProtoDataStore
import com.hejwesele.events.dto.EventSettingsDto
import com.hejwesele.events.mappers.mapDto
import com.hejwesele.events.mappers.mapModel
import com.hejwesele.events.model.Event
import com.hejwesele.events.model.EventSettings
import com.hejwesele.protodatastore.ProtoDataStore
import com.hejwesele.protodatastore.ProtoMessageSpecification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProtoEventsLocalSource @Inject constructor(
    @EventsProtoDataStore private val eventsStore: ProtoDataStore<EventSettingsDto>
) : EventsLocalSource {

    override suspend fun getEventSettings(): Result<EventSettings> = withContext(Dispatchers.IO) {
        eventsStore.readData().mapCatching { it.mapModel() }
    }

    override suspend fun setEvent(event: Event): Result<Event> = withContext(Dispatchers.IO) {
        eventsStore.writeData { it.copy(event = event.mapDto()) }.map { event }
    }

    override suspend fun setGalleryHintDismissed(dismissed: Boolean): Result<Boolean> = withContext(Dispatchers.IO) {
        eventsStore.writeData { it.copy(galleryHintDismissed = dismissed) }.map { dismissed }
    }

    companion object {
        const val EVENTS_DATASTORE_FILE = "events"

        val eventSettingsSpecification = ProtoMessageSpecification(
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
