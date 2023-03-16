package com.hejwesele.settings

import com.hejwesele.settings.model.Event
import com.hejwesele.settings.model.EventSettings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val localSource: SettingsLocalSource
) {

    suspend fun setEvent(event: Event): Result<Event> =
        localSource.setEvent(event)

    suspend fun setGalleryHintDismissed(dismissed: Boolean): Result<Boolean> =
        localSource.setGalleryHintDismissed(dismissed)

    suspend fun getStoredSettings(): Result<EventSettings> =
        localSource.getStoredSettings()
}
