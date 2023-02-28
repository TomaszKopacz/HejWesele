package com.hejwesele.settings.terms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hejwesele.settings.navigation.SettingsFeatureNavigation
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun TermsAndConditions(
    navigation: SettingsFeatureNavigation
) {
    TermsAndConditionsScreen(navigation)
}

@Composable
private fun TermsAndConditionsScreen(
    navigation: SettingsFeatureNavigation,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Go to T&C")
    }
}
