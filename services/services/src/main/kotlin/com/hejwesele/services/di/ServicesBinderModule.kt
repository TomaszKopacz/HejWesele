package com.hejwesele.services.di

import com.hejwesele.services.RemoteDatabaseServicesRemoteSource
import com.hejwesele.services.ServicesRemoteSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface ServicesBinderModule {

    @Binds
    fun bindServicesRemoteSource(impl: RemoteDatabaseServicesRemoteSource): ServicesRemoteSource
}
