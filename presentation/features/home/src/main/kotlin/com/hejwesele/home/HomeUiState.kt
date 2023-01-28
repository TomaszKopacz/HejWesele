package com.hejwesele.home

import com.hejwesele.home.model.HomeTileUiModel

internal data class HomeUiState(
    val isLoading: Boolean,
    val tiles: List<HomeTileUiModel>
) {
    companion object {
        val DEFAULT = HomeUiState(
            isLoading = true,
            tiles = emptyList()
        )
    }
}
