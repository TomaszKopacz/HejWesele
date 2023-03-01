package com.hejwesele.main

import androidx.annotation.DrawableRes

internal sealed class MainTabItem(
    val route: String,
    @DrawableRes val iconSelected: Int,
    @DrawableRes val iconUnselected: Int
) {
    object Home : MainTabItem(
        route = MainRoutes.home,
        iconSelected = R.drawable.ic_home_filled,
        iconUnselected = R.drawable.ic_home
    )

    object Schedule : MainTabItem(
        route = MainRoutes.schedule,
        iconSelected = R.drawable.ic_schedule_filled,
        iconUnselected = R.drawable.ic_schedule
    )

    object Services : MainTabItem(
        route = MainRoutes.services,
        iconSelected = R.drawable.ic_services_filled,
        iconUnselected = R.drawable.ic_services
    )

    object Gallery : MainTabItem(
        route = MainRoutes.gallery,
        iconSelected = R.drawable.ic_media_filled,
        iconUnselected = R.drawable.ic_media
    )
}
