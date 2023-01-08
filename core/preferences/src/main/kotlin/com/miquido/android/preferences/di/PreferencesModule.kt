package com.miquido.android.preferences.di

import com.miquido.android.preferences.Preferences
import com.miquido.android.preferences.PreferencesFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class PreferencesModule {

    @Provides
    @Singleton
    fun provideDefaultPreferences(
        factory: PreferencesFactory
    ): Preferences = factory.create(Preferences.DEFAULT)
}
