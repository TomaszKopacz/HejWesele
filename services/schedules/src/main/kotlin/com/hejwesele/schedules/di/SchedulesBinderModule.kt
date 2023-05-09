package com.hejwesele.schedules.di

import com.hejwesele.schedules.RemoteDatabaseSchedulesRemoteSource
import com.hejwesele.schedules.SchedulesRemoteSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface SchedulesBinderModule {

    @Binds
    fun bindSchedulesRemoteSource(impl: RemoteDatabaseSchedulesRemoteSource): SchedulesRemoteSource
}
