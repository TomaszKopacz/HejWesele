package com.hejwesele.galleries.di

import com.hejwesele.galleries.FirebaseGalleriesRemoteSource
import com.hejwesele.galleries.GalleriesRemoteSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface GalleriesBinderModule {

    @Binds
    fun bindGalleriesRemoteSource(impl: FirebaseGalleriesRemoteSource): GalleriesRemoteSource
}
