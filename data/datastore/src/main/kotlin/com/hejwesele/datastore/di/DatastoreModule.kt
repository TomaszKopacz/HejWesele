package com.hejwesele.datastore.di

import com.hejwesele.datastore.Datastore
import com.hejwesele.datastore.Firestore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface DatastoreModule {

    @Binds
    @Singleton
    fun bindDatastore(impl: Firestore): Datastore
}
