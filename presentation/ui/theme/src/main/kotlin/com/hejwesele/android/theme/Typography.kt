package com.hejwesele.android.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hejwesele.theme.R

val Lato = FontFamily(
    listOf(
        Font(R.font.lato_light, FontWeight.Light),
        Font(R.font.lato_regular, FontWeight.Normal),
        Font(R.font.lato_bold, FontWeight.SemiBold),
        Font(R.font.lato_black, FontWeight.Bold)
    )
)

internal val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = Lato,
        fontSize = 96.sp,
        fontWeight = FontWeight.Light,
        letterSpacing = (-1.5).sp
    ),
    displayMedium = TextStyle(
        fontFamily = Lato,
        fontSize = 60.sp,
        fontWeight = FontWeight.Light,
        letterSpacing = (-0.5).sp
    ),
    displaySmall = TextStyle(
        fontFamily = Lato,
        fontSize = 48.sp,
        fontWeight = FontWeight.Normal
    ),
    headlineLarge = TextStyle(
        fontFamily = Lato,
        fontSize = 44.sp,
        fontWeight = FontWeight.Light,
        letterSpacing = 0.5.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Lato,
        fontSize = 34.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.25.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Lato,
        fontSize = 24.sp,
        fontWeight = FontWeight.Normal
    ),
    titleLarge = TextStyle(
        fontFamily = Lato,
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.15.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Lato,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = Lato,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Lato,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Lato,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Lato,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Lato,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.4.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Lato,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.8.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Lato,
        fontSize = 10.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.15.sp
    )
)
