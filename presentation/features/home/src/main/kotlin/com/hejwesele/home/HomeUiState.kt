package com.hejwesele.home

data class HomeUiState(
    val eventName: String
) {
    companion object {
        val DEFAULT = HomeUiState(eventName = "Loading...")
    }
}
