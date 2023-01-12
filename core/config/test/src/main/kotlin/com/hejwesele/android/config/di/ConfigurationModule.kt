package com.hejwesele.android.config.di

import com.hejwesele.android.config.CONFIGURATION_PREFERENCES
import com.hejwesele.android.config.Configuration
import com.hejwesele.android.config.ConfigurationPreferences
import com.hejwesele.android.config.ConfigurationRepository
import com.hejwesele.android.config.ConfigurationRepositoryImpl
import com.hejwesele.android.preferences.Preferences
import com.hejwesele.android.preferences.PreferencesFactory
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
