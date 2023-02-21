package com.hejwesele.settings.licenses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hejwesele.android.components.HeaderMedium
import com.hejwesele.android.customtabs.LocalCustomTabs
import com.hejwesele.settings.R
import com.hejwesele.settings.navigation.SettingsNavGraph
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Destination
@SettingsNavGraph
internal fun Licenses(viewModel: LicensesViewModel = hiltViewModel()) {
    val uiState by viewModel.states.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item { HeaderMedium(text = stringResource(id = R.string.licenses_title)) }
        item {
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
                text = stringResource(id = R.string.licenses_intro),
                style = MaterialTheme.typography.body2
            )
        }
        items(uiState.items) { item ->
            ListItem(
                text = { Text(item.title) },
                secondaryText = { Link(text = item.linkText, url = item.linkUrl) }
            )
        }
    }
}

@Composable
private fun Link(text: String, url: String) {
    val customTabs = LocalCustomTabs.current

    Row(
        modifier = Modifier
            .wrapContentWidth(align = Alignment.Start)
            .clickable { customTabs.launch(url) }
    ) {
        Text(
            modifier = Modifier.weight(1f, fill = false),
            text = text,
            color = MaterialTheme.colors.primary
        )
        Icon(
            modifier = Modifier
                .padding(top = 2.dp)
                .padding(horizontal = 4.dp)
                .size(16.dp),
            imageVector = Icons.Filled.Link,
            tint = MaterialTheme.colors.primary,
            contentDescription = null
        )
    }
}
