package com.hejwesele.android.datetime.di

import com.hejwesele.android.datetime.Clock
import com.hejwesele.android.datetime.ClockImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DateTimeModule {

    @Binds
    fun bindClock(impl: ClockImpl): Clock
}
