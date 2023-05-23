package com.hejwesele.services.model

internal data class ServiceDetailsUiModel(
    val name: String,
    val imageUrl: String,
    val details: List<Pair<String, List<String>>>,
    val intents: List<IntentUiModel>
)
