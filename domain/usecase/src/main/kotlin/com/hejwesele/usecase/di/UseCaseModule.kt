package com.hejwesele.usecase.di

import com.hejwesele.repository.IEventRepository
import com.hejwesele.usecase.event.GetEvent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class UseCaseModule {

    @Provides
    @Singleton
    fun providesGetEventUseCase(repository: IEventRepository) = GetEvent(repository)
}
