package com.hejwesele.gallery.board.usecase

import com.hejwesele.settings.SettingsRepository
import javax.inject.Inject

class DismissGalleryHint @Inject constructor(
    private val repository: SettingsRepository
) {

    suspend operator fun invoke() = repository.setGalleryHintDismissed(true)
}
