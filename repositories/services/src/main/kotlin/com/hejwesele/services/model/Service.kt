package com.hejwesele.services.model

import com.hejwesele.intent.IntentData

data class Service(
    val id: String,
    val type: ServiceType,
    val title: String,
    val name: String?,
    val description: String,
    val details: List<ServiceDetails>,
    val image: String,
    val intents: List<IntentData>
)
