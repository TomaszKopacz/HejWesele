package com.hejwesele.events.di

import android.content.Context
import com.hejwesele.datastore.DataStore
import com.hejwesele.events.DataStoreEventsLocalSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class EventsModule {

    @Provides
    @Singleton
    @EventsDataStore
    fun provideEventsDataStore(
        @ApplicationContext context: Context
    ) = DataStore(
        fileName = DataStoreEventsLocalSource.EVENTS_DATASTORE_FILE,
        specification = DataStoreEventsLocalSource.eventSettingsSpecification,
        context = context
    )
}
