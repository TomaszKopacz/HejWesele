package com.hejwesele.configuration.di

import android.content.Context
import com.hejwesele.datastore.DataStore
import com.hejwesele.configuration.DataStoreConfigurationLocalSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class ConfigurationModule {

    @Provides
    @Singleton
    @ConfigurationDataStore
    fun provideConfigurationDataStore(
        @ApplicationContext context: Context
    ) = DataStore(
        fileName = DataStoreConfigurationLocalSource.CONFIGURATION_DATASTORE_FILE,
        specification = DataStoreConfigurationLocalSource.configurationSpecification,
        context = context
    )
}
