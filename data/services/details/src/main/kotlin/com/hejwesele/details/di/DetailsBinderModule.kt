package com.hejwesele.details.di

import com.hejwesele.details.DetailsRemoteSource
import com.hejwesele.details.RemoteDatabaseDetailsRemoteSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DetailsBinderModule {

    @Binds
    fun bindDetailsRemoteSource(impl: RemoteDatabaseDetailsRemoteSource): DetailsRemoteSource
}
