package com.miquido.android.thememanager.di

import com.miquido.android.thememanager.AppCompatDelegateWrapper
import com.miquido.android.thememanager.AppCompatDelegateWrapperImpl
import com.miquido.android.thememanager.ThemeManager
import com.miquido.android.thememanager.ThemeManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface ThemeManagerModule {

    @Binds
    fun bindThemeManager(impl: ThemeManagerImpl): ThemeManager

    @Binds
    fun bindAppCompatDelegateWrapper(impl: AppCompatDelegateWrapperImpl): AppCompatDelegateWrapper
}
