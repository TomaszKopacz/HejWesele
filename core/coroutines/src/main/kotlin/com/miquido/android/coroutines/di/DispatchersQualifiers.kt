package com.miquido.android.coroutines.di

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class DefaultDispatcher

@Qualifier
@Retention(RUNTIME)
annotation class IoDispatcher

@Qualifier
@Retention(RUNTIME)
annotation class MainDispatcher

@Qualifier
@Retention(RUNTIME)
annotation class MainImmediateDispatcher
