package com.hejwesele.regulations.di

import com.hejwesele.regulations.RegulationsRemoteSource
import com.hejwesele.regulations.RemoteDatabaseRegulationsRemoteSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface RegulationsBinderModule {

    @Binds
    fun bindRegulationsRemoteSource(impl: RemoteDatabaseRegulationsRemoteSource): RegulationsRemoteSource
}
