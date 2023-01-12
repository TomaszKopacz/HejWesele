package com.hejwesele.android.customtabs

import androidx.compose.runtime.staticCompositionLocalOf

val LocalCustomTabs = staticCompositionLocalOf<CustomTabs> {
    error("CompositionLocal LocalCustomTabs not present")
}
