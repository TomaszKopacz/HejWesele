package com.miquido.android.datetime.di

import com.miquido.android.datetime.Clock
import com.miquido.android.datetime.TestClock
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DateTimeModule::class]
)
class TestDateTimeModule {

    @Provides
    fun provideClock(): Clock = TestClock
}
