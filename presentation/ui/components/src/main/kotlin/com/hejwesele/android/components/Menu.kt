package com.hejwesele.android.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.hejwesele.android.theme.Dimension

@Composable
fun MenuComponent(
    modifier: Modifier = Modifier,
    iconResId: Int,
    items: List<MenuItemData>
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(modifier = modifier) {
        Icon(
            modifier = Modifier
                .size(Dimension.iconNormal)
                .clip(MaterialTheme.shapes.small)
                .clickable(onClick = { expanded = true }),
            painter = painterResource(iconResId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { data ->
                DropdownMenuItem(
                    modifier = Modifier.clip(MaterialTheme.shapes.large),
                    text = { Text(data.text) },
                    onClick = {
                        expanded = false
                        data.action()
                    }
                )
            }
        }
    }
}

data class MenuItemData(
    val text: String,
    val action: () -> Unit
)
