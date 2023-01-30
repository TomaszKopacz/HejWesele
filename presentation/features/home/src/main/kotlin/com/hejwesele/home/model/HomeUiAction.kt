package com.hejwesele.home.model

internal sealed class HomeUiAction {
    class ShowTileIntentOptions(val intents: List<IntentUiModel>) : HomeUiAction()
    class OpenActivity(val intent: IntentUiModel) : HomeUiAction()
}
