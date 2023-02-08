package com.hejwesele.settings

import com.hejwesele.result.CompletableResult
import com.hejwesele.settings.model.EventSettings
import com.hejwesele.result.Result
import com.hejwesele.settings.model.Event

interface SettingsLocalSource {

    suspend fun getStoredSettings(): Result<EventSettings>

    suspend fun setEvent(event: Event): CompletableResult

    suspend fun setGalleryHintDismissed(dismissed: Boolean): CompletableResult
}
