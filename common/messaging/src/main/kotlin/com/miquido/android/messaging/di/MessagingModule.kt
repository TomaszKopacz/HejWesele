package com.miquido.android.messaging.di

import com.miquido.android.messaging.FirebaseMessaging
import com.miquido.android.messaging.Messaging
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface MessagingModule {

    @Binds
    @Singleton
    fun bindMessaging(impl: FirebaseMessaging): Messaging
}
