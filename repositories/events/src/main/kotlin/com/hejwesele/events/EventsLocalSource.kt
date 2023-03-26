package com.hejwesele.events

import com.hejwesele.events.model.EventSettings

interface EventsLocalSource {

    suspend fun getEventSettings(): Result<EventSettings>

    suspend fun setEventSettings(eventSettings: EventSettings): Result<EventSettings>

    suspend fun setGalleryHintDismissed(dismissed: Boolean): Result<Boolean>
}
