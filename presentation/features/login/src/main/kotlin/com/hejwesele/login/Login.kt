package com.hejwesele.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun Login(
    navigation: ILoginFeatureNavigation
) {
    LoginScreen(navigation)
}

@Composable
private fun LoginScreen(
    navigation: ILoginFeatureNavigation
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { navigation.openEvent() }
        ) {
            Text(text = "Open event")
        }
    }
}
