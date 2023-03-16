package com.hejwesele.gallery.board.usecase

import com.hejwesele.settings.SettingsRepository
import com.hejwesele.settings.model.EventSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetEventSettings @Inject constructor(
    private val repository: SettingsRepository
) {

    suspend operator fun invoke(): Result<EventSettings> = withContext(Dispatchers.IO) {
        repository.getStoredSettings()
    }
}
