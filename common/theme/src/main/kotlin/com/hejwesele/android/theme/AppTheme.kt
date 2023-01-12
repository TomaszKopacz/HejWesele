package com.hejwesele.android.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightThemeColors = lightColors(
    primary = NavyBlue,
    primaryVariant = DarkBlue,
    onPrimary = Color.White,
    secondary = Orange,
    secondaryVariant = Orange,
    onSecondary = Color.Black
)

private val DarkThemeColors = darkColors(
    primary = NavyBlue,
    primaryVariant = DarkBlue,
    onPrimary = Color.White,
    secondary = Orange,
    secondaryVariant = Orange,
    onSecondary = Color.Black
)

@Composable
fun AppTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    MaterialTheme(
        colors = if (darkTheme) DarkThemeColors else LightThemeColors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
