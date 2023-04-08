package com.hejwesele.legals.di

import com.hejwesele.legals.LegalDocumentsRemoteSource
import com.hejwesele.legals.RemoteDatabaseLegalDocumentsRemoteSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface LegalDocumentsBinderModule {

    @Binds
    fun bindLegalDocumentsRemoteSource(impl: RemoteDatabaseLegalDocumentsRemoteSource): LegalDocumentsRemoteSource
}
