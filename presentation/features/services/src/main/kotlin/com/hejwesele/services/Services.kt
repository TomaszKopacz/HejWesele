package com.hejwesele.services

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hejwesele.android.components.TextBodyLarge
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun Services() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(
                WindowInsets
                    .statusBars
                    .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
                    .asPaddingValues()
            )
    ) {
        TextBodyLarge(
            modifier = Modifier.align(Alignment.Center),
            text = "Partnerzy i atrkacje"
        )
    }
}
