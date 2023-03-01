package com.hejwesele.navigation.features

import androidx.compose.runtime.Composable
import com.hejwesele.gallery.IGalleryNavigation
import com.hejwesele.gallery.board.Gallery
import com.hejwesele.home.IHomeNavigation
import com.hejwesele.home.home.Home
import com.hejwesele.main.IMainFeatureProvider
import com.hejwesele.schedule.Schedule
import com.hejwesele.services.Services

class MainFeatureProvider(
    private val homeFeatureNavigation: IHomeNavigation,
    private val galleryFeatureNavigation: IGalleryNavigation
) : IMainFeatureProvider {

    override fun home(): @Composable () -> Unit = { Home(navigation = homeFeatureNavigation) }

    override fun schedule(): @Composable () -> Unit = { Schedule() }

    override fun services(): @Composable () -> Unit = { Services() }

    override fun gallery(): @Composable () -> Unit = { Gallery(navigation = galleryFeatureNavigation) }
}
