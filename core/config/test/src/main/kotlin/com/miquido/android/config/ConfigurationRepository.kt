package com.miquido.android.config

import com.miquido.android.config.Environment.SANDBOX
import com.miquido.android.preferences.Preferences
import com.miquido.android.preferences.key
import javax.inject.Inject
import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
internal annotation class ConfigurationPreferences

internal const val CONFIGURATION_PREFERENCES = "configuration"

internal interface ConfigurationRepository {
    fun getAll(): List<Configuration>
    suspend fun getCurrentEnvironment(): Environment
    suspend fun setCurrentEnvironment(environment: Environment)
    suspend fun getCurrentConfiguration(): Configuration
}

internal class ConfigurationRepositoryImpl @Inject constructor(
    @ConfigurationPreferences private val preferences: Preferences
) : ConfigurationRepository {

    override fun getAll(): List<Configuration> = listOf(
        SandboxConfiguration,
        StagingConfiguration,
        ProductionConfiguration
    )

    override suspend fun getCurrentEnvironment(): Environment {
        return preferences.get(CURRENT_ENVIRONMENT)?.let<String, Environment> { enumValueOf(it) } ?: DEFAULT_ENVIRONMENT
    }

    override suspend fun setCurrentEnvironment(environment: Environment) {
        preferences.put(CURRENT_ENVIRONMENT to environment.name)
    }

    override suspend fun getCurrentConfiguration(): Configuration {
        val current = getCurrentEnvironment()
        return getAll().first { it.environment == current }
    }

    internal companion object {
        private val DEFAULT_ENVIRONMENT = SANDBOX
        private val CURRENT_ENVIRONMENT = key<String>("current_environment")
    }
}
