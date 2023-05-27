package com.hejwesele.configuration.di

import com.hejwesele.configuration.ConfigurationLocalSource
import com.hejwesele.configuration.DataStoreConfigurationLocalSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface ConfigurationBinderModule {

    @Binds
    fun bindConfigurationLocalSource(impl: DataStoreConfigurationLocalSource): ConfigurationLocalSource
}
