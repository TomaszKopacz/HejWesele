package com.hejwesele.gallery.usecase

import com.hejwesele.result.Result
import com.hejwesele.settings.SettingsRepository
import com.hejwesele.settings.model.EventSettings
import javax.inject.Inject

class GetEventSettings @Inject constructor(
    private val repository: SettingsRepository
) {

    suspend operator fun invoke(): Result<EventSettings> = repository.getStoredSettings()
}
