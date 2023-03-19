package com.hejwesele.events.di

import android.content.Context
import com.hejwesele.protodatastore.ProtoDataStore
import com.hejwesele.events.ProtoEventsLocalSource
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
    @EventsProtoDataStore
    fun provideSettingsDataStore(
        @ApplicationContext context: Context
    ) = ProtoDataStore(
        fileName = ProtoEventsLocalSource.EVENTS_DATASTORE_FILE,
        specification = ProtoEventsLocalSource.eventSettingsSpecification,
        context = context
    )
}
