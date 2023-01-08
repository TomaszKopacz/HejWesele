package com.miquido.android.thememanager

import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.miquido.android.coroutines.di.IoDispatcher
import com.miquido.android.osinfo.OsInfo
import com.miquido.android.preferences.Preferences
import com.miquido.android.preferences.key
import com.miquido.android.thememanager.Theme.DARK
import com.miquido.android.thememanager.Theme.LIGHT
import com.miquido.android.thememanager.Theme.SYSTEM_DEFAULT
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

internal class ThemeManagerImpl @Inject constructor(
    private val osInfo: OsInfo,
    private val preferences: Preferences,
    private val appCompatDelegateWrapper: AppCompatDelegateWrapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ThemeManager {

    companion object {
        private val KEY = key<String>("theme")
    }

    override fun getAvailableThemes(): List<Theme> {
        return listOfNotNull(
            if (osInfo.isQOrHigher) SYSTEM_DEFAULT else null,
            LIGHT,
            DARK
        )
    }

    override fun observeSelectedTheme(): Flow<Theme> {
        return preferences
            .observe(KEY)
            .map { if (it != null) Theme.valueOf(it.uppercase()) else getDefaultTheme() }
            .flowOn(ioDispatcher)
    }

    override fun getSelectedTheme(): Theme {
        return runBlocking { loadTheme() }
    }

    override suspend fun switchTheme(theme: Theme) {
        setAppCompatTheme(theme)
        preferences.put(KEY to theme.name.lowercase())
    }

    override fun applyTheme() {
        val theme = runBlocking { loadTheme() }
        setAppCompatTheme(theme)
    }

    private suspend fun loadTheme(): Theme {
        return preferences.get(KEY)
            ?.let { Theme.valueOf(it.uppercase()) }
            ?: getDefaultTheme()
    }

    private fun getDefaultTheme(): Theme {
        return if (osInfo.isQOrHigher) SYSTEM_DEFAULT else LIGHT
    }

    private fun setAppCompatTheme(theme: Theme) {
        appCompatDelegateWrapper.setDefaultNightMode(
            when (theme) {
                SYSTEM_DEFAULT -> MODE_NIGHT_FOLLOW_SYSTEM
                LIGHT -> MODE_NIGHT_NO
                DARK -> MODE_NIGHT_YES
            }
        )
    }
}
