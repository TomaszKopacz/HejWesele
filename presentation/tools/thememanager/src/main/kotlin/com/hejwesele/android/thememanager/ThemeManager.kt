package com.hejwesele.android.thememanager

import com.hejwesele.configuration.model.Theme
import kotlinx.coroutines.flow.Flow

interface ThemeManager {
    fun getAvailableThemes(): List<Theme>
    fun observeSelectedTheme(): Flow<Theme>
    fun getSelectedTheme(): Theme
    suspend fun switchTheme(theme: Theme)
    fun applyTheme()
}
