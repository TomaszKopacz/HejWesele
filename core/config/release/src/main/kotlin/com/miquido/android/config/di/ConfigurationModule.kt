package com.miquido.android.config.di

import com.miquido.android.config.Configuration
import com.miquido.android.config.ProductionConfiguration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal class ConfigurationModule {
    @Provides
    fun provideConfiguration(): Configuration = ProductionConfiguration
}
