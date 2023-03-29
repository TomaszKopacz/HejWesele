package com.hejwesele.android.components.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hejwesele.android.components.VerticalMargin
import com.hejwesele.android.theme.Dimension

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetScaffold(
    state: ModalBottomSheetState,
    padding: PaddingValues? = null,
    sheetContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    ModalBottomSheetLayout(
        sheetState = state,
        sheetShape = RoundedCornerShape(
            topStart = Dimension.radiusRoundedCornerLarge,
            topEnd = Dimension.radiusRoundedCornerLarge
        ),
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        sheetContent = {
            val bottomPadding = WindowInsets.navigationBars
                .only(WindowInsetsSides.Bottom)
                .asPaddingValues()
                .calculateBottomPadding()

            val paddingValues = padding ?: PaddingValues(
                start = Dimension.marginLarge,
                end = Dimension.marginLarge,
                top = Dimension.marginNormal,
                bottom = bottomPadding
            )

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
            ) {
                Box(
                    Modifier
                        .height(Dimension.bottomSheetHandleHeight)
                        .width(Dimension.bottomSheetHandleWidth)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .align(Alignment.CenterHorizontally)
                )
                VerticalMargin(Dimension.marginLarge)
                sheetContent()
                VerticalMargin(Dimension.marginSmall)
            }
        },
        content = { content() }
    )
}
