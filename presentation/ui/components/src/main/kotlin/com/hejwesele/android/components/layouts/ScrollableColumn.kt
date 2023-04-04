package com.hejwesele.android.components.layouts

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScrollableColumn(
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    arrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Box(modifier = modifier) {
        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        state = rememberScrollState(),
                        enabled = true
                    ),
                horizontalAlignment = alignment,
                verticalArrangement = arrangement,
                content = content
            )
        }
    }
}
