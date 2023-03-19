package com.hejwesele.android.thememanager

import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.hejwesele.android.coroutines.di.IoDispatcher
import com.hejwesele.android.osinfo.OsInfo
import com.hejwesele.settings.ConfigurationRepository
import com.hejwesele.settings.model.Configuration
import com.hejwesele.settings.model.Theme
import com.hejwesele.settings.model.Theme.DARK
import com.hejwesele.settings.model.Theme.LIGHT
import com.hejwesele.settings.model.Theme.SYSTEM_DEFAULT
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

internal class ThemeManagerImpl @Inject constructor(
    private val osInfo: OsInfo,
    private val repository: ConfigurationRepository,
    private val appCompatDelegateWrapper: AppCompatDelegateWrapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ThemeManager {

    override fun getAvailableThemes(): List<Theme> {
        return listOfNotNull(
            if (osInfo.isQOrHigher) SYSTEM_DEFAULT else null,
            LIGHT,
            DARK
        )
    }

    override fun observeSelectedTheme(): Flow<Theme> {
        return repository.observeStoredConfiguration()
            .map { it.theme }
            .flowOn(ioDispatcher)
    }

    override fun getSelectedTheme(): Theme {
        return runBlocking {
            val theme = loadTheme()
            if (theme.isSuccess) {
                theme.getOrDefault(getDefaultTheme())
            } else {
                getDefaultTheme()
            }
        }
    }

    override suspend fun switchTheme(theme: Theme) {
        setAppCompatTheme(theme)

        repository.storeConfiguration(
            Configuration(theme = theme)
        )
    }

    override fun applyTheme() {
        runBlocking {
            loadTheme()
                .onSuccess { setAppCompatTheme(it) }
                .onFailure { setAppCompatTheme(SYSTEM_DEFAULT) }
        }
    }

    private fun getDefaultTheme(): Theme {
        return if (osInfo.isQOrHigher) SYSTEM_DEFAULT else LIGHT
    }

    private suspend fun loadTheme(): Result<Theme> {
        return repository.getStoredConfiguration()
            .mapCatching { configuration -> configuration.theme }
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
