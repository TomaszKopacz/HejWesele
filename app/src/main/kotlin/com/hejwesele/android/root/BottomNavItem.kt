package com.hejwesele.android.root

import androidx.annotation.DrawableRes

data class BottomNavItem(
    val route: String,
    @DrawableRes val iconSelected: Int,
    @DrawableRes val iconUnselected: Int
)