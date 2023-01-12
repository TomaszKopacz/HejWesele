package com.hejwesele.android.thememanager

import kotlinx.coroutines.flow.Flow

enum class Theme {
    SYSTEM_DEFAULT, LIGHT, DARK
}

interface ThemeManager {
    fun getAvailableThemes(): List<Theme>
    fun observeSelectedTheme(): Flow<Theme>
    fun getSelectedTheme(): Theme
    suspend fun switchTheme(theme: Theme)
    fun applyTheme()
}
