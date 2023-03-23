package com.hejwesele.settings.mappers

import com.hejwesele.settings.dto.ConfigurationDto
import com.hejwesele.settings.dto.ThemeDto
import com.hejwesele.settings.model.Configuration
import com.hejwesele.settings.model.Theme

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
