package com.hejwesele.events

import com.hejwesele.events.model.Event
import com.hejwesele.events.model.EventSettings

interface EventsLocalSource {

    suspend fun getEventSettings(): Result<EventSettings>

    suspend fun setEvent(event: Event): Result<Event>

    suspend fun setGalleryHintDismissed(dismissed: Boolean): Result<Boolean>
}
