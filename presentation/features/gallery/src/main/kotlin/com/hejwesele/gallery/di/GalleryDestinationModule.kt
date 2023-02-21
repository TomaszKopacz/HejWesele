package com.hejwesele.gallery.di

import com.hejwesele.android.navigation.FeatureModules
import com.hejwesele.android.navigation.ModuleDestination
import com.hejwesele.android.navigation.ModuleDestinationSpec.DirectDestinationSpec
import com.hejwesele.gallery.destinations.GalleryDestination
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@InstallIn(SingletonComponent::class)
@Module
internal class GalleryDestinationModule {
    @Provides
    @IntoMap
    @StringKey(FeatureModules.GALLERY)
    fun provideDestination(): ModuleDestination = DirectDestinationSpec(GalleryDestination)
}
