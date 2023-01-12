package com.hejwesele.android.config

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

object ConfigurationSwitcher {

    @Suppress("UnusedPrivateMember", "FunctionOnlyReturningConstant")
    fun isSwitching(context: Context): Boolean = false

    @Suppress("UnusedPrivateMember", "EmptyFunctionBlock")
    @Composable
    fun Switcher(modifier: Modifier = Modifier, onClick: () -> Unit) {
    }
}
