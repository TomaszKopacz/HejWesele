package com.hejwesele.services.dto

import androidx.annotation.Keep

@Keep
data class ServiceDto(
    val type: String = "drink",
    val title: String? = null,
    val name: String? = null,
    val description: String? = null,
    val details: List<ServiceDetailsDto> = emptyList(),
    val image: String? = null,
    val urls: List<String> = emptyList()
)
