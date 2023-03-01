package com.hejwesele.settings

import com.hejwesele.result.CompletableResult
import com.hejwesele.result.Result
import com.hejwesele.settings.model.Event
import com.hejwesele.settings.model.EventSettings

interface SettingsLocalSource {

    suspend fun getStoredSettings(): Result<EventSettings>

    suspend fun setEvent(event: Event): CompletableResult

    suspend fun setGalleryHintDismissed(dismissed: Boolean): CompletableResult
}
