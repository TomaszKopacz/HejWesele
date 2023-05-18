package com.hejwesele.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.hejwesele.android.theme.Alpha
import com.hejwesele.android.theme.AppTheme
import com.hejwesele.android.theme.Dimension

@Composable
fun HyperlinkText(
    modifier: Modifier = Modifier,
    text: String,
    links: Map<String, () -> Unit>,
    style: TextStyle,
    linkStyle: TextStyle = style
) {
    val actionTag = "ACTION_TAG"

    val annotatedString = buildAnnotatedString {
        append(text)

        links.forEach { (link, _) ->
            val startIndex = text.indexOf(link)
            val endIndex = startIndex + link.length

            addStyle(
                style = style.toSpanStyle(),
                start = 0,
                end = text.length
            )

            addStyle(
                style = linkStyle.toSpanStyle(),
                start = startIndex,
                end = endIndex
            )

            addStringAnnotation(
                tag = actionTag,
                annotation = link,
                start = startIndex,
                end = endIndex
            )
        }
    }

    ClickableText(
        modifier = modifier,
        text = annotatedString,
        overflow = TextOverflow.Visible,
        onClick = { index ->
            annotatedString
                .getStringAnnotations(actionTag, index, index)
                .firstOrNull()?.item?.let { annotation ->
                    links[annotation]?.invoke()
                }
        }
    )
}

@Composable
fun TextPlaceholder(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(Dimension.marginLarge)
            .fillMaxSize()
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.alpha(Alpha.alpha50)
        )
    }
}

@Preview
@Composable
private fun TextPlaceholderPreview() {
    AppTheme(darkTheme = false) {
        TextPlaceholder(
            modifier = Modifier
                .background(Color.White)
                .padding(Dimension.marginNormal),
            text = "Test placeholder message"
        )
    }
}

@Preview
@Composable
private fun HyperlinkTextPreview() {
    AppTheme(darkTheme = false) {
        HyperlinkText(
            modifier = Modifier
                .background(Color.White)
                .padding(Dimension.marginNormal),
            text = "This is test.",
            links = mapOf("test" to {}),
            style = MaterialTheme.typography.bodySmall,
            linkStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.outline
            )
        )
    }
}
