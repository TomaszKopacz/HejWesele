package com.hejwesele.information.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import com.hejwesele.android.components.HorizontalMargin
import com.hejwesele.android.components.VerticalMargin
import com.hejwesele.android.theme.Dimension
import com.hejwesele.legaldocument.LegalPoint
import com.hejwesele.legaldocument.LegalPointType
import com.hejwesele.information.ui.LegalPointItemConstants.GAP
import kotlin.math.min

@Composable
internal fun LegalPointItem(
    legalPoint: LegalPoint,
    typography: Typography
) {
    val style = getTextStyle(regulationPointType = legalPoint.type, typography = typography)
    val symbol = getSymbol(regulationPointType = legalPoint.type)
    val prefix = getPrefix(order = legalPoint.order, symbol = symbol)
    val indent = getIndent(prefix = prefix, level = legalPoint.level)
    val indicator = indent + prefix
    val space = getSpace(regulationPointType = legalPoint.type)

    Row {
        if (indicator.isNotEmpty()) {
            Text(
                text = indicator,
                style = style
            )
            HorizontalMargin(Dimension.marginNormal)
        }
        Text(
            text = legalPoint.text,
            style = style
        )
    }
    VerticalMargin(space)
}

private object LegalPointItemConstants {
    const val GAP = 5
}

private fun getTextStyle(
    regulationPointType: LegalPointType,
    typography: Typography
) = when (regulationPointType) {
    LegalPointType.TITLE -> typography.headlineLarge
    LegalPointType.PARAGRAPH -> typography.titleSmall
    else -> typography.bodySmall
}

private fun getSymbol(regulationPointType: LegalPointType) = when (regulationPointType) {
    LegalPointType.PARAGRAPH -> "§"
    LegalPointType.BULLET_POINT -> "●"
    else -> null
}

private fun getPrefix(order: String?, symbol: String?) =
    order?.let { o ->
        symbol?.let { s -> "$s $o." } ?: "$o."
    }.orEmpty()

private fun getIndent(prefix: String, level: String) = if (prefix.isEmpty()) {
    " ".repeat(level.toInt() * GAP)
} else {
    " ".repeat((level.toInt() + 1) * GAP - min(prefix.length, GAP))
}

private fun getSpace(regulationPointType: LegalPointType) = when (regulationPointType) {
    LegalPointType.TITLE -> Dimension.marginLarge
    LegalPointType.PARAGRAPH -> Dimension.marginSmall
    else -> Dimension.marginSmall
}
