package com.hejwesele.event

import androidx.annotation.DrawableRes
import com.hejwesele.event.navigation.EventRoutes

internal sealed class EventTabItem(
    val route: String,
    @DrawableRes val iconSelected: Int,
    @DrawableRes val iconUnselected: Int
) {
    object Home : EventTabItem(
        route = EventRoutes.home,
        iconSelected = R.drawable.ic_home_filled,
        iconUnselected = R.drawable.ic_home
    )

    object Schedule : EventTabItem(
        route = EventRoutes.schedule,
        iconSelected = R.drawable.ic_schedule_filled,
        iconUnselected = R.drawable.ic_schedule
    )

    object Services : EventTabItem(
        route = EventRoutes.services,
        iconSelected = R.drawable.ic_services_filled,
        iconUnselected = R.drawable.ic_services
    )

    object Gallery : EventTabItem(
        route = EventRoutes.gallery,
        iconSelected = R.drawable.ic_media_filled,
        iconUnselected = R.drawable.ic_media
    )
}
