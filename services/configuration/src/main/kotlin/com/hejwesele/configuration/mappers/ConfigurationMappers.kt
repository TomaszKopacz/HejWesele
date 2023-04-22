package com.hejwesele.configuration.mappers

import com.hejwesele.configuration.dto.ConfigurationDto
import com.hejwesele.configuration.dto.ThemeDto
import com.hejwesele.configuration.model.Configuration
import com.hejwesele.configuration.model.Theme

internal fun Configuration.mapDto() = ConfigurationDto(
    theme = theme.mapDto()
)

internal fun ConfigurationDto.mapModel() = Configuration(
    theme = theme.mapModel()
)

internal fun Theme.mapDto() = when (this) {
    Theme.SYSTEM_DEFAULT -> ThemeDto.SYSTEM_DEFAULT
    Theme.LIGHT -> ThemeDto.LIGHT
    Theme.DARK -> ThemeDto.DARK
}

internal fun ThemeDto.mapModel() = when (this) {
    ThemeDto.SYSTEM_DEFAULT -> Theme.SYSTEM_DEFAULT
    ThemeDto.LIGHT -> Theme.LIGHT
    ThemeDto.DARK -> Theme.DARK
}
