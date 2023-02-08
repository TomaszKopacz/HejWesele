package com.hejwesele.home

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.mvvm.StateActionsViewModel
import com.hejwesele.home.constants.Strings
import com.hejwesele.home.model.InvitationTileUiModel
import com.hejwesele.home.model.HomeUiAction
import com.hejwesele.home.model.HomeUiAction.OpenActivity
import com.hejwesele.home.model.HomeUiAction.ShowTileIntentOptions
import com.hejwesele.home.model.HomeUiState
import com.hejwesele.home.model.IntentUiModel
import com.hejwesele.home.usecase.GetEvent
import com.hejwesele.home.usecase.ObserveInvitation
import com.hejwesele.home.usecase.StoreEvent
import com.hejwesele.invitations.model.IntentType
import com.hejwesele.invitations.model.IntentUrl
import com.hejwesele.invitations.model.IntentUrlPrefix
import com.hejwesele.invitations.model.Invitation
import com.hejwesele.invitations.model.InvitationTile
import com.hejwesele.invitations.model.InvitationTileType
import com.hejwesele.result.Result
import com.hejwesele.result.onError
import com.hejwesele.result.onSuccess
import com.hejwesele.result.thenCompletable
import com.hejwesele.settings.model.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val observeInvitation: ObserveInvitation,
    private val getEvent: GetEvent,
    private val storeEvent: StoreEvent
) : StateActionsViewModel<HomeUiState, HomeUiAction>(HomeUiState.DEFAULT) {

    init {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            getEvent("")
                .thenCompletable {
                    storeEvent(Event(
                        eventId = it.id,
                        eventName = it.name,
                        date = it.date,
                        invitationId = it.invitationId,
                        galleryId = it.galleryId
                    ))
                }

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

    private suspend fun handleInvitationResult(result: Result<Invitation>) {
        result
            .onSuccess { invitation ->
                updateState {
                    copy(isLoading = false, tiles = invitation.tiles.map { it.toUiModel() })
                }
            }
            .onError { error ->
                updateState { copy(isLoading = false, error = error) }
            }
    }

    private fun InvitationTile.toUiModel() = InvitationTileUiModel(
        type = type,
        title = title,
        subtitle = subtitle ?: "",
        description = description ?: "",
        avatars = avatars,
        animationResId = getAnimationResource(type),
        intents = intents.map { it.toUiModel() },
        clickable = intents.isNotEmpty()
    )

    private fun IntentUrl.toUiModel() = IntentUiModel(
        title = url.getIntentTitle(type),
        iconResId = getIntentIconResId(type),
        intentPackage = intentPackage,
        url = url
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
    }

    private fun String.getIntentTitle(type: IntentType) = when (type) {
        IntentType.INSTAGRAM -> removePrefix(IntentUrlPrefix.INSTAGRAM).split('/').first()
        IntentType.GOOGLE_MAPS -> Strings.intentTitleGoogleMaps
        else -> Strings.intentTitleWww
    }
}
