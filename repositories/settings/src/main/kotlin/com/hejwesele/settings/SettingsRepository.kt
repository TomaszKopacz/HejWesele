package com.hejwesele.settings

import com.hejwesele.settings.model.Event
import com.hejwesele.settings.model.EventSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val localSource: SettingsLocalSource
) {

    suspend fun setEvent(event: Event): Result<Event> = withContext(Dispatchers.IO) {
        localSource.setEvent(event)
    }

    suspend fun setGalleryHintDismissed(dismissed: Boolean): Result<Boolean> = withContext(Dispatchers.IO) {
        localSource.setGalleryHintDismissed(dismissed)
    }

    suspend fun getStoredSettings(): Result<EventSettings> = withContext(Dispatchers.IO) {
        localSource.getStoredSettings()
    }
}
