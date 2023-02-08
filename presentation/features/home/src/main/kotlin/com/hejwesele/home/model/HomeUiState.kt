package com.hejwesele.home.model

import com.hejwesele.result.GeneralError

internal data class HomeUiState(
    val isLoading: Boolean,
    val tiles: List<InvitationTileUiModel>,
    val intents: List<IntentUiModel>,
    val error: GeneralError?
) {
    companion object {
        val DEFAULT = HomeUiState(
            isLoading = true,
            tiles = emptyList(),
            intents = emptyList(),
            error = null
        )
    }
}
