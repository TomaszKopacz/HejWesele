package com.hejwesele.home.home.usecase

import com.hejwesele.settings.SettingsRepository
import com.hejwesele.settings.model.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class StoreEvent @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(event: Event) = withContext(Dispatchers.IO) {
        repository.setEvent(event)
    }
}
