package com.hejwesele.gallery.usecase

import com.hejwesele.result.CompletableResult
import com.hejwesele.settings.SettingsRepository
import javax.inject.Inject

class DismissGalleryHint @Inject constructor(
    private val repository: SettingsRepository
) {

    suspend operator fun invoke(): CompletableResult =
        repository.setGalleryHintDismissed(true)
}
