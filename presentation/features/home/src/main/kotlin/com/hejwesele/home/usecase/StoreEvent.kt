package com.hejwesele.home.usecase


import com.hejwesele.settings.SettingsRepository
import com.hejwesele.settings.model.Event
import javax.inject.Inject

class StoreEvent @Inject constructor(
    private val repository: SettingsRepository
) {

    suspend operator fun invoke(event: Event) = repository.setEvent(event)
}
