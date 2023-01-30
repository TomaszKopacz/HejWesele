package com.hejwesele.home.model

internal data class HomeUiState(
    val isLoading: Boolean,
    val tiles: List<HomeTileUiModel>,
    val intents: List<IntentUiModel>
) {
    companion object {
        val DEFAULT = HomeUiState(
            isLoading = true,
            tiles = emptyList(),
            intents = emptyList()
        )
    }
}
