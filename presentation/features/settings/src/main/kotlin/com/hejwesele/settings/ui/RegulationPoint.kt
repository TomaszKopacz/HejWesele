package com.hejwesele.settings.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import com.hejwesele.android.components.HorizontalMargin
import com.hejwesele.android.components.VerticalMargin
import com.hejwesele.android.theme.Dimension
import com.hejwesele.regulations.model.RegulationPoint
import com.hejwesele.regulations.model.RegulationPointType
import com.hejwesele.settings.ui.RegulationItemConstants.GAP
import kotlin.math.min

@Composable
internal fun RegulationItem(
    regulationPoint: RegulationPoint,
    typography: Typography
) {
    val style = getTextStyle(regulationPointType = regulationPoint.type, typography = typography)
    val symbol = getSymbol(regulationPointType = regulationPoint.type)
    val prefix = getPrefix(order = regulationPoint.order, symbol = symbol)
    val indent = getIndent(prefix = prefix, level = regulationPoint.level)
    val indicator = indent + prefix
    val space = getSpace(regulationPointType = regulationPoint.type)

    Row {
        if (indicator.isNotEmpty()) {
            Text(
                text = indicator,
                style = style
            )
            HorizontalMargin(Dimension.marginNormal)
        }
        Text(
            text = regulationPoint.text,
            style = style
        )
    }
    VerticalMargin(space)
}

private object RegulationItemConstants {
    const val GAP = 5
}

private fun getTextStyle(
    regulationPointType: RegulationPointType,
    typography: Typography
) = when (regulationPointType) {
    RegulationPointType.TITLE -> typography.displaySmall
    RegulationPointType.PARAGRAPH -> typography.titleMedium
    else -> typography.bodyMedium
}

private fun getSymbol(regulationPointType: RegulationPointType) = when (regulationPointType) {
    RegulationPointType.PARAGRAPH -> "§"
    RegulationPointType.BULLET_POINT -> "●"
    else -> null
}

private fun getPrefix(order: String?, symbol: String?) =
    order?.let { o ->
        symbol?.let { s -> "$s $o." } ?: "$o."
    }.orEmpty()

private fun getIndent(prefix: String, level: Int) = if (prefix.isEmpty()) {
    " ".repeat(level * GAP)
} else {
    " ".repeat((level + 1) * GAP - min(prefix.length, GAP))
}

private fun getSpace(regulationPointType: RegulationPointType) = when (regulationPointType) {
    RegulationPointType.TITLE -> Dimension.marginLarge
    RegulationPointType.PARAGRAPH -> Dimension.marginNormal
    else -> Dimension.marginSmall
}
