package com.hejwesele.settings

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewModelScope
import com.hejwesele.android.appinfo.AppInfo
import com.hejwesele.android.mvvm.StateViewModel
import com.hejwesele.android.thememanager.ThemeManager
import com.hejwesele.settings.SettingsUiItemType.LICENSES
import com.hejwesele.settings.SettingsUiItemType.LOGOUT
import com.hejwesele.settings.SettingsUiItemType.THEME_SWITCHER
import com.hejwesele.settings.navigation.SettingsNavigator
import com.hejwesele.settings.theme.text
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

internal data class SettingsUiState(
    val items: Map<SettingsUiItemType, SettingsUiItem> = linkedMapOf(
        THEME_SWITCHER to SettingsUiItem(icon = Icons.Filled.Palette, text = R.string.settings_theme),
        LICENSES to SettingsUiItem(icon = Icons.Filled.Palette, text = R.string.settings_theme),
        LOGOUT to SettingsUiItem(icon = Icons.Filled.Palette, text = R.string.settings_theme)
    ),
    val appInformation: String
)

internal enum class SettingsUiItemType {
    THEME_SWITCHER, LICENSES, LOGOUT
}

internal data class SettingsUiItem(
    val icon: ImageVector,
    @StringRes val text: Int,
    @StringRes val secondaryText: Int? = null
)

@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    appInfo: AppInfo,
    private val themeManager: ThemeManager,
    private val navigator: SettingsNavigator
) : StateViewModel<SettingsUiState>(SettingsUiState(appInformation = "$${appInfo.appName} ${appInfo.versionName} (${appInfo.versionCode})")) {

    init {
        viewModelScope.launch {
            themeManager.observeSelectedTheme()
                .collect { theme ->
                    updateState {
                        copy(
                            items = items + (THEME_SWITCHER to items.getValue(THEME_SWITCHER).copy(secondaryText = theme.text())))
                    }
                }
        }
    }

    fun onItemClicked(type: SettingsUiItemType) = viewModelScope.launch {
        when (type) {
            THEME_SWITCHER -> navigator.openThemeSwitcher()
            LICENSES -> navigator.openLicenses()
            LOGOUT -> navigator.openLogin()
        }
    }
}