package com.hejwesele.repository.di

import com.hejwesele.repository.EventRepository
import com.hejwesele.repository.IEventRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {

    @Binds
    fun bindEventRepository(impl: EventRepository): IEventRepository
}
