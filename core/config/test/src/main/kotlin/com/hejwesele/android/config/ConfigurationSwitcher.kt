package com.hejwesele.android.config

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jakewharton.processphoenix.ProcessPhoenix

object ConfigurationSwitcher {

    fun isSwitching(context: Context): Boolean =
        ProcessPhoenix.isPhoenixProcess(context)

    @Composable
    fun Switcher(modifier: Modifier = Modifier, onClick: () -> Unit) {
        FloatingActionButton(
            modifier = modifier.padding(all = 16.dp),
            onClick = onClick
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_settings_24),
                contentDescription = null
            )
        }
    }
}
