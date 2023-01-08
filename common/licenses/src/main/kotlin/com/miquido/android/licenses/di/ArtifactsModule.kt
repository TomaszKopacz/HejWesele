package com.miquido.android.licenses.di

import com.miquido.android.licenses.ArtifactsRepository
import com.miquido.android.licenses.AssetsArtifactsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface ArtifactsModule {

    @Binds
    fun bindArtifactsRepository(impl: AssetsArtifactsRepository): ArtifactsRepository
}
