package com.hejwesele.home

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateActionsViewModel
import com.hejwesele.events.model.HomeTile
import com.hejwesele.events.model.HomeTileType
import com.hejwesele.events.model.IntentType
import com.hejwesele.events.model.IntentUrl
import com.hejwesele.events.model.UrlPrefix
import com.hejwesele.home.constants.Strings
import com.hejwesele.home.model.HomeTileUiModel
import com.hejwesele.home.model.HomeUiAction
import com.hejwesele.home.model.HomeUiAction.OpenActivity
import com.hejwesele.home.model.HomeUiAction.ShowTileIntentOptions
import com.hejwesele.home.model.HomeUiState
import com.hejwesele.home.model.IntentUiModel
import com.hejwesele.home.usecase.GetHomeTiles
import com.hejwesele.result.onError
import com.hejwesele.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val getHomeTiles: GetHomeTiles
) : StateActionsViewModel<HomeUiState, HomeUiAction>(HomeUiState.DEFAULT) {

    init {
        viewModelScope.launch {
            getHomeTiles("-NNcZtoxOovK9JxKWeNN")
                .onSuccess { homeTiles ->
                    updateState {
                        copy(
                            isLoading = false,
                            tiles = homeTiles.map { it.toUiModel() }
                        )
                    }
                }
                .onError {
                    updateState {
                        copy(
                            isLoading = false,
                            tiles = emptyList()
                        )
                    }
                }
        }
    }

    fun onTileClicked(tile: HomeTileUiModel) {
        viewModelScope.launch {
            val intents = tile.intents
            if (intents.isNotEmpty()) {
                updateState { copy(intents = tile.intents) }
                emitAction(ShowTileIntentOptions(intents))
            } else {
                updateState { copy(intents = emptyList()) }
            }
        }
    }

    fun onTileIntentOptionSelected(intent: IntentUiModel) {
        viewModelScope.launch {
            emitAction(OpenActivity(intent))
        }
    }

    private fun HomeTile.toUiModel() = HomeTileUiModel(
        type = type,
        title = title,
        subtitle = subtitle,
        description = description,
        photoUrls = photoUrls,
        animationResId = getAnimationResource(type),
        intents = intents?.map { it.toUiModel() } ?: emptyList(),
        clickable = intents?.isNotEmpty() ?: false
    )

    private fun IntentUrl.toUiModel() = IntentUiModel(
        title = url.getIntentTitle(type),
        iconResId = getIntentIconResId(type),
        intentPackage = intentPackage,
        url = url
    )

    private fun getAnimationResource(tileType: HomeTileType) = when (tileType) {
        HomeTileType.COUPLE -> R.raw.lottie_heart
        HomeTileType.DATE -> R.raw.lottie_calendar
        HomeTileType.CHURCH -> R.raw.lottie_church
        HomeTileType.VENUE -> R.raw.lottie_home
        HomeTileType.WISHES -> R.raw.lottie_toast
    }

    private fun getIntentIconResId(type: IntentType) = when (type) {
        IntentType.INSTAGRAM -> R.drawable.ic_instagram_primary
        IntentType.GOOGLE_MAPS -> R.drawable.ic_maps_primary
        IntentType.WWW -> R.drawable.ic_web_primary
    }

    private fun String.getIntentTitle(type: IntentType) = when (type) {
        IntentType.INSTAGRAM -> removePrefix(UrlPrefix.INSTAGRAM).split('/').first()
        IntentType.GOOGLE_MAPS -> Strings.intentTitleGoogleMaps
        else -> Strings.intentTitleWww
    }
}
