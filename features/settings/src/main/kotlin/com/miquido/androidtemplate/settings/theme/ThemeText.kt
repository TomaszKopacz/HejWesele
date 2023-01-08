package com.miquido.androidtemplate.settings.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import com.miquido.android.thememanager.Theme
import com.miquido.androidtemplate.settings.R

@Composable
@ReadOnlyComposable
internal fun themeText(theme: Theme?): String {
    return if (theme == null) {
        ""
    } else {
        stringResource(
            id = when (theme) {
                Theme.SYSTEM_DEFAULT -> R.string.theme_system_default
                Theme.LIGHT -> R.string.theme_light
                Theme.DARK -> R.string.theme_dark
            }
        )
    }
}
