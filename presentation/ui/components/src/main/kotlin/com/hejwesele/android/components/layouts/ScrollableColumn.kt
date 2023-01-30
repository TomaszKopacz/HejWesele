package com.hejwesele.android.components.layouts

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScrollableColumn(
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(
                        state = rememberScrollState(),
                        enabled = true
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                content = content
            )
        }
    }
}
