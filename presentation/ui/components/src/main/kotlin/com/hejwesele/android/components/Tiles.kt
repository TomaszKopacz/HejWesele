package com.hejwesele.android.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.hejwesele.android.theme.Dimension
import com.hejwesele.theme.R

@Composable
fun HintTile(
    modifier: Modifier = Modifier,
    text: String,
    onCloseClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.tertiaryContainer,
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier
                .padding(Dimension.marginNormal)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_info),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1f, true)
                    .padding(horizontal = Dimension.marginSmall)
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier.clickable { onCloseClick() }
            )
        }
    }
}
