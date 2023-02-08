package com.hejwesele.settings.di

import android.content.Context
import com.hejwesele.protodatastore.ProtoDataStore
import com.hejwesele.settings.ProtoDataStoreSettingsLocalSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class SettingsModule {

    @Provides
    @Singleton
    @SettingsProtoDataStore
    fun provideSettingsDataStore(
        @ApplicationContext context: Context
    ) = ProtoDataStore(
        fileName = ProtoDataStoreSettingsLocalSource.SETTINGS_DATASTORE_FILE,
        specification = ProtoDataStoreSettingsLocalSource.settingsSpecification,
        context = context
    )
}
