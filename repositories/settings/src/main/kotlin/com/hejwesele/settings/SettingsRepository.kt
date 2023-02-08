package com.hejwesele.settings

import com.hejwesele.result.CompletableResult
import com.hejwesele.result.Result
import com.hejwesele.settings.model.Event
import com.hejwesele.settings.model.EventSettings
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val localSource: SettingsLocalSource
) {

    suspend fun setEvent(event: Event): CompletableResult =
        localSource.setEvent(event)

    suspend fun setGalleryHintDismissed(dismissed: Boolean): CompletableResult =
        localSource.setGalleryHintDismissed(dismissed)

    suspend fun getStoredSettings(): Result<EventSettings> =
        localSource.getStoredSettings()
}
