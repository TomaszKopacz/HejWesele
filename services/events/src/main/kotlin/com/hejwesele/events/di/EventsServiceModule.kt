package com.hejwesele.events.di

import com.hejwesele.events.EventsRemoteSource
import com.hejwesele.events.remote.FirebaseEventsRemoteSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface EventsServiceModule {

    @Binds
    fun bindEventRemoteSource(impl: FirebaseEventsRemoteSource): EventsRemoteSource
}
