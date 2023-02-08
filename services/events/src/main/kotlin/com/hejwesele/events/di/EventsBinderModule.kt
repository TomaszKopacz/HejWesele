package com.hejwesele.events.di

import com.hejwesele.events.EventsRemoteSource
import com.hejwesele.events.FirebaseEventsRemoteSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface EventsBinderModule {

    @Binds
    fun bindEventRemoteSource(impl: FirebaseEventsRemoteSource): EventsRemoteSource
}
