package com.hejwesele.services.dto

import androidx.annotation.Keep

@Keep
data class ServiceDto(
    val id: String? = null,
    val type: String = "drink",
    val title: String? = null,
    val name: String? = null,
    val description: String? = null,
    val details: List<ServiceDetailsDto> = emptyList(),
    val image: String = "",
    val urls: List<String> = emptyList()
)
