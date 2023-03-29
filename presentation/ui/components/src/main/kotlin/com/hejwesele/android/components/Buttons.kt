package com.hejwesele.android.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.hejwesele.android.components.PlainButtonSize.LARGE
import com.hejwesele.android.components.PlainButtonSize.MEDIUM
import com.hejwesele.android.components.PlainButtonSize.SMALL
import com.hejwesele.android.theme.AppTheme
import com.hejwesele.android.theme.Dimension
import com.hejwesele.components.R
import com.hejwesele.extensions.disabled

@Composable
fun PlainButton(
    modifier: Modifier = Modifier,
    text: String,
    size: PlainButtonSize = MEDIUM,
    color: Color = MaterialTheme.colorScheme.onBackground,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Text(
            text = text,
            style = when (size) {
                SMALL -> MaterialTheme.typography.titleSmall
                MEDIUM -> MaterialTheme.typography.titleMedium
                LARGE -> MaterialTheme.typography.titleLarge
            },
            color = color
        )
    }
}

@Composable
fun PlainIconButton(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes icon: Int,
    color: Color = MaterialTheme.colorScheme.onBackground,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(Dimension.iconNormal)
        )
        HorizontalMargin(Dimension.marginSmall)
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            color = color
        )
    }
}

@Composable
fun FilledButton(
    modifier: Modifier = Modifier,
    text: String,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    color: Color = MaterialTheme.colorScheme.onPrimary,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            disabledBackgroundColor = backgroundColor.disabled,
            contentColor = color,
            disabledContentColor = color.disabled
        ),
        contentPadding = PaddingValues(
            horizontal = Dimension.marginOutsizeExtraLarge,
            vertical = Dimension.marginLarge
        ),
        shape = MaterialTheme.shapes.small,
        onClick = onClick
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun FloatingButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    enabled: Boolean,
    action: () -> Unit
) {
    val iconColor = MaterialTheme.colorScheme.onTertiaryContainer

    Surface(
        modifier = modifier
            .shadow(
                elevation = Dimension.elevationSmall,
                shape = MaterialTheme.shapes.large
            )
            .clickable(enabled = enabled) { action() },
        color = MaterialTheme.colorScheme.tertiaryContainer,
        shape = MaterialTheme.shapes.large
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (enabled) iconColor else iconColor.disabled,
            modifier = Modifier.padding(Dimension.marginNormal)
        )
    }
}

enum class PlainButtonSize {
    SMALL, MEDIUM, LARGE
}

@Preview
@Composable
private fun PlainButtonPreview() {
    AppTheme(darkTheme = false) {
        PlainButton(
            text = "Submit",
            color = MaterialTheme.colorScheme.primary,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun PlainIconButtonPreview() {
    AppTheme(darkTheme = false) {
        PlainIconButton(
            text = "SUBMIT",
            icon = R.drawable.ic_download,
            color = MaterialTheme.colorScheme.primary,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun FilledButtonPreview() {
    AppTheme(darkTheme = false) {
        FilledButton(
            text = "NEXT",
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun FloatingButtonPreview() {
    AppTheme(darkTheme = false) {
        FloatingButton(
            icon = Icons.Default.Add,
            enabled = true,
            action = {}
        )
    }
}
