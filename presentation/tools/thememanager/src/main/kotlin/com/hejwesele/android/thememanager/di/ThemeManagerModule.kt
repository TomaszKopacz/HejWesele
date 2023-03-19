package com.hejwesele.android.thememanager.di

import com.hejwesele.android.thememanager.AppCompatDelegateWrapper
import com.hejwesele.android.thememanager.AppCompatDelegateWrapperImpl
import com.hejwesele.android.thememanager.ThemeManager
import com.hejwesele.android.thememanager.ThemeManagerImpl
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
