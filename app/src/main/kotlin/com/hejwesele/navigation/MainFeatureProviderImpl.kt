package com.hejwesele.navigation

import androidx.compose.runtime.Composable
import com.hejwesele.event.navigation.MainFeatureProvider
import com.hejwesele.gallery.Gallery
import com.hejwesele.home.Home
import com.hejwesele.home.navigation.HomeFeatureNavigation
import com.hejwesele.schedule.Schedule
import com.hejwesele.schedule.navigation.ScheduleFeatureNavigation
import com.hejwesele.services.Services

class MainFeatureProviderImpl(
    private val homeFeatureNavigation: HomeFeatureNavigation,
    private val scheduleFeatureNavigation: ScheduleFeatureNavigation
) : MainFeatureProvider {

    override fun home(): @Composable () -> Unit = { Home(navigation = homeFeatureNavigation) }

    override fun schedule(): @Composable () -> Unit = { Schedule() }

    override fun services(): @Composable () -> Unit = { Services() }

    override fun gallery(): @Composable () -> Unit = { Gallery() }
}
