package com.hejwesele.home.di

import com.hejwesele.events.EventsRepository
import com.hejwesele.home.usecase.GetEvents
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal class HomeModule {

    @Provides
    fun providesGetEventsUseCase(repository: EventsRepository) = GetEvents(repository)
}
