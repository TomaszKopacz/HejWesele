package com.hejwesele.settings.di

import com.hejwesele.settings.navigation.SettingsNavigator
import com.hejwesele.settings.navigation.SettingsNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal interface SettingsNavigationModule {

    @Binds
    fun bindNavigator(impl: SettingsNavigatorImpl): SettingsNavigator
}
