package com.hejwesele.settings.theme

import com.hejwesele.android.thememanager.Theme
import com.hejwesele.settings.R

internal fun Theme.text() = when (this) {
    Theme.SYSTEM_DEFAULT -> R.string.theme_system_default
    Theme.LIGHT -> R.string.theme_light
    Theme.DARK -> R.string.theme_dark
}
