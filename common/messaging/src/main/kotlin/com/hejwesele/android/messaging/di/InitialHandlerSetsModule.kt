package com.hejwesele.android.messaging.di

import com.hejwesele.android.messaging.MessageHandler
import com.hejwesele.android.messaging.TokenHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ElementsIntoSet

/*
 * Dagger fails when set of objects is about to be injected and there are no elements provided.
 * Provide empty sets of objects of a given type, with @ElementsIntoSet annotation, as a workaround.
 */
@Module
@InstallIn(SingletonComponent::class)
internal class InitialHandlerSetsModule {

    @Provides
    @ElementsIntoSet
    fun provideInitialTokenHandlersSet(): Set<TokenHandler> = emptySet()

    @Provides
    @ElementsIntoSet
    fun provideInitialMessageHandlerSet(): Set<MessageHandler> = emptySet()
}
