package com.hejwesele.settings

import com.hejwesele.settings.model.Configuration
import com.hejwesele.settings.model.Theme
import kotlinx.coroutines.flow.Flow

interface ConfigurationLocalSource {

    suspend fun getConfiguration(): Result<Configuration>

    fun observeConfiguration(): Flow<Configuration>

    suspend fun setConfiguration(configuration: Configuration): Result<Configuration>

    suspend fun setAppTheme(theme: Theme): Result<Theme>
}
