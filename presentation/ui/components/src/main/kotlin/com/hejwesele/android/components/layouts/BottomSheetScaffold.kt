package com.hejwesele.android.components.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
    sheetContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    ModalBottomSheetLayout(
        sheetState = state,
        sheetShape = RoundedCornerShape(
            topStart = Dimension.radiusRoundedCornerNormal,
            topEnd = Dimension.radiusRoundedCornerNormal
        ),
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        sheetContent = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = Dimension.marginLarge,
                        vertical = Dimension.marginNormal
                    )
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
            }
        },
        content = { content() }
    )
}
