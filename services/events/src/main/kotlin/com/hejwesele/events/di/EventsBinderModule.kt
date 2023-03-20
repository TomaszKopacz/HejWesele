package com.hejwesele.events.di

import com.hejwesele.events.EventsLocalSource
import com.hejwesele.events.EventsRemoteSource
import com.hejwesele.events.RemoteDatabaseEventsRemoteSource
import com.hejwesele.events.DataStoreEventsLocalSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface EventsBinderModule {

    @Binds
    fun bindEventRemoteSource(impl: RemoteDatabaseEventsRemoteSource): EventsRemoteSource

    @Binds
    fun bindEventLocalSource(impl: DataStoreEventsLocalSource): EventsLocalSource
}
