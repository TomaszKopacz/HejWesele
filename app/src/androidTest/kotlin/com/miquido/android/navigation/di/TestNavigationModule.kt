package com.miquido.android.navigation.di

import com.miquido.android.navigation.Navigation
import com.miquido.android.navigation.Navigator
import com.miquido.android.navigation.TestNavigation
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NavigationModule::class]
)
class TestNavigationModule {

    @Provides
    fun provideNavigation(): Navigation = TestNavigation

    @Provides
    fun provideNavigator(): Navigator = TestNavigation
}
