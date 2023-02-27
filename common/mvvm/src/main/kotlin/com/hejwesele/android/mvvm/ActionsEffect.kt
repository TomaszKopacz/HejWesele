package com.hejwesele.android.mvvm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@Composable
fun <UiAction> ActionsEffect(
    actions: SharedFlow<UiAction>,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    handler: suspend (UiAction) -> Unit
) {
    DisposableEffect(Unit) {
        val job = coroutineScope.launch {
            actions.collect { handler.invoke(it) }
        }
        onDispose { job.cancel() }
    }
}
