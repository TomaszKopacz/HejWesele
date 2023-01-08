package com.miquido.android.mvvm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun <UiAction> ActionsEffect(
    actionsHandler: ActionsHandler<UiAction>,
    coroutineScope: CoroutineScope,
    handler: suspend (UiAction) -> Unit
) {
    DisposableEffect(Unit) {
        val job = coroutineScope.launch {
            actionsHandler.actions.collect { handler.invoke(it) }
        }
        onDispose { job.cancel() }
    }
}
