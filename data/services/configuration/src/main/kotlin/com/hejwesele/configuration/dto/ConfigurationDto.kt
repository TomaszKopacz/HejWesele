package com.hejwesele.configuration.dto

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class ConfigurationDto(
    val theme: ThemeDto = ThemeDto.SYSTEM_DEFAULT
)
