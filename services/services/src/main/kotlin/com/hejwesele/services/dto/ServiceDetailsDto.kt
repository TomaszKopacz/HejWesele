package com.hejwesele.services.dto

import androidx.annotation.Keep

@Keep
data class ServiceDetailsDto(
    val title: String? = null,
    val content: List<String> = emptyList()
)
