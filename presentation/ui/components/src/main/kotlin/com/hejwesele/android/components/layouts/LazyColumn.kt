package com.hejwesele.android.components.layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun LazyListScope.margin(size: Dp) {
    item { Spacer(Modifier.height(size)) }
}

fun LazyListScope.singleItem(
    content: @Composable () -> Unit
) {
    item {
        content()
    }
}

fun LazyListScope.expandedItem(
    content: @Composable () -> Unit
) {
    item {
        Box(modifier = Modifier.fillParentMaxHeight()) {
            content()
        }
    }
}

fun <T> LazyListScope.gridItems(
    items: List<T>,
    columnCount: Int,
    padding: Dp = 0.dp,
    innerPadding: Dp = 0.dp,
    itemContent: @Composable BoxScope.(Int) -> Unit
) {
    val itemsCount = items.count()
    val rowsCount = if (itemsCount == 0) 0 else 1 + (itemsCount - 1) / columnCount

    items(
        count = rowsCount,
        key = { it.hashCode() }
    ) { rowIndex ->
        Column {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = padding)
            ) {
                for (columnIndex in 0 until columnCount) {
                    val itemIndex = rowIndex * columnCount + columnIndex
                    if (itemIndex < itemsCount) {
                        Box(
                            modifier = Modifier
                                .weight(1f, true)
                                .aspectRatio(1f),
                            propagateMinConstraints = true
                        ) {
                            itemContent(itemIndex)
                        }
                        if (columnIndex != columnCount - 1 && columnCount > 1) {
                            Spacer(Modifier.width(innerPadding))
                        }
                    } else {
                        Spacer(Modifier.weight(1f, true))
                    }
                }
            }
            if (rowIndex < rowsCount - 1) {
                Spacer(Modifier.height(innerPadding))
            }
        }
    }
}
