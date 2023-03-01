package com.hejwesele.settings.overview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hejwesele.settings.ISettingsFeatureNavigation
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun SettingsOverview(
    navigation: ISettingsFeatureNavigation
) {
    SettingsOverviewScreen(navigation)
}

@Composable
private fun SettingsOverviewScreen(
    navigation: ISettingsFeatureNavigation
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { navigation.openTermsAndConditions() }
        ) {
            Text(text = "Go to T&C")
        }
    }
}
