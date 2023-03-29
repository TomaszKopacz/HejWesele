package com.hejwesele.home.home

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateViewModel
import com.hejwesele.android.theme.Label
import com.hejwesele.home.R
import com.hejwesele.home.home.model.IntentUiModel
import com.hejwesele.home.home.model.InvitationTileUiModel
import com.hejwesele.home.home.usecase.ObserveInvitation
import com.hejwesele.intent.IntentData
import com.hejwesele.intent.IntentType
import com.hejwesele.intent.IntentUrlPrefix
import com.hejwesele.invitations.model.Invitation
import com.hejwesele.invitations.model.InvitationTile
import com.hejwesele.invitations.model.InvitationTileType
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val observeInvitation: ObserveInvitation
) : StateViewModel<HomeUiState>(HomeUiState.DEFAULT) {

    init {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            observeInvitation("")
                .collect { result ->
                    handleInvitationResult(result)
                }
        }
    }

    fun onTileClicked(tile: InvitationTileUiModel) {
        viewModelScope.launch {
            val intents = tile.intents
            if (intents.isNotEmpty()) {
                updateState { copy(showTileOptions = triggered, intents = tile.intents) }
            } else {
                updateState { copy(intents = emptyList()) }
            }
        }
    }

    fun onTileIntentOptionSelected(intent: IntentUiModel) {
        viewModelScope.launch {
            updateState { copy(hideTileOptions = triggered, openIntent = triggered(intent)) }
        }
    }

    fun onTileOptionsShown() {
        updateState { copy(showTileOptions = consumed) }
    }

    fun onTileOptionsHidden() {
        updateState { copy(hideTileOptions = consumed) }
    }

    fun onIntentOpened() {
        updateState { copy(openIntent = consumed()) }
    }

    fun onErrorRetry() {
        // no-op
    }

    private fun handleInvitationResult(result: Result<Invitation>) {
        result
            .onSuccess { invitation ->
                updateState {
                    copy(isLoading = false, tiles = invitation.tiles.map { it.toUiModel() })
                }
            }
            .onFailure { error ->
                updateState { copy(isLoading = false, error = error) }
            }
    }

    private fun InvitationTile.toUiModel() = InvitationTileUiModel(
        type = type,
        title = title,
        subtitle = subtitle.orEmpty(),
        description = description.orEmpty(),
        avatars = avatars,
        animationResId = getAnimationResource(type),
        intents = intents.map { it.toUiModel() },
        clickable = intents.isNotEmpty()
    )

    private fun IntentData.toUiModel() = IntentUiModel(
        title = intentUrl.getIntentTitle(intentType),
        iconResId = getIntentIconResId(intentType),
        intentPackage = intentPackage,
        url = intentUrl
    )

    private fun getAnimationResource(tileType: InvitationTileType) = when (tileType) {
        InvitationTileType.COUPLE -> R.raw.lottie_heart
        InvitationTileType.DATE -> R.raw.lottie_calendar
        InvitationTileType.CHURCH -> R.raw.lottie_church
        InvitationTileType.VENUE -> R.raw.lottie_home
        InvitationTileType.WISHES -> R.raw.lottie_toast
    }

    private fun getIntentIconResId(type: IntentType) = when (type) {
        IntentType.INSTAGRAM -> R.drawable.ic_instagram_primary
        IntentType.GOOGLE_MAPS -> R.drawable.ic_maps_primary
        IntentType.WWW -> R.drawable.ic_web_primary
        else -> R.drawable.ic_web_primary
    }

    private fun String.getIntentTitle(type: IntentType) = when (type) {
        IntentType.INSTAGRAM -> removePrefix(IntentUrlPrefix.INSTAGRAM).split('/').first()
        IntentType.GOOGLE_MAPS -> Label.homeIntentMapsLabel
        else -> Label.homeIntentWebLabel
    }
}

internal data class HomeUiState(
    val openIntent: StateEventWithContent<IntentUiModel>,
    val showTileOptions: StateEvent,
    val hideTileOptions: StateEvent,
    val isLoading: Boolean,
    val tiles: List<InvitationTileUiModel>,
    val intents: List<IntentUiModel>,
    val error: Throwable?
) {
    companion object {
        val DEFAULT = HomeUiState(
            showTileOptions = consumed,
            hideTileOptions = consumed,
            openIntent = consumed(),
            isLoading = true,
            tiles = emptyList(),
            intents = emptyList(),
            error = null
        )
    }
}
