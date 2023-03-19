package com.hejwesele.settings.di

import com.hejwesele.settings.ConfigurationLocalSource
import com.hejwesele.settings.ProtoConfigurationLocalSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface ConfigurationBinderModule {

    @Binds
    fun bindConfigurationLocalSource(impl: ProtoConfigurationLocalSource): ConfigurationLocalSource
}