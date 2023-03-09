package com.hejwesele.settings

import com.hejwesele.settings.model.Event
import com.hejwesele.settings.model.EventSettings

interface SettingsLocalSource {

    suspend fun getStoredSettings(): Result<EventSettings>

    suspend fun setEvent(event: Event): Result<Event>

    suspend fun setGalleryHintDismissed(dismissed: Boolean): Result<Boolean>
}
