package com.hejwesele.services.dto

import androidx.annotation.Keep

@Keep
data class ServicesDto(
    val attractions: List<ServiceDto> = emptyList(),
    val partners: List<ServiceDto> = emptyList()
)
