package com.miquido.android.config.di

import com.miquido.android.config.CONFIGURATION_PREFERENCES
import com.miquido.android.config.Configuration
import com.miquido.android.config.ConfigurationPreferences
import com.miquido.android.config.ConfigurationRepository
import com.miquido.android.config.ConfigurationRepositoryImpl
import com.miquido.android.preferences.Preferences
import com.miquido.android.preferences.PreferencesFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal class ConfigurationModule {
    @Provides
    @Singleton
    fun provideConfiguration(repo: ConfigurationRepository): Configuration = runBlocking {
        repo.getCurrentConfiguration()
    }

    @Provides
    @ConfigurationPreferences
    fun provideConfigurationPreferences(factory: PreferencesFactory): Preferences = factory.create(CONFIGURATION_PREFERENCES)

    @Provides
    @Singleton
    fun provideConfigurationRepository(impl: ConfigurationRepositoryImpl): ConfigurationRepository = impl
}
