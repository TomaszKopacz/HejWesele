package com.hejwesele.home.home

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.components.AlertData
import com.hejwesele.android.components.ErrorData
import com.hejwesele.android.mvvm.StateEventsViewModel
import com.hejwesele.android.theme.Label
import com.hejwesele.events.model.EventSettings
import com.hejwesele.home.R
import com.hejwesele.home.home.model.IntentUiModel
import com.hejwesele.home.home.model.InvitationTileUiModel
import com.hejwesele.home.home.usecase.GetEventSettings
import com.hejwesele.home.home.usecase.Logout
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.hejwesele.theme.R as ThemeR

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val getEventSettings: GetEventSettings,
    private val observeInvitation: ObserveInvitation,
    private val logout: Logout
) : StateEventsViewModel<HomeUiState, HomeUiEvents>(HomeUiState.Default, HomeUiEvents.Default) {

    init {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            getEventSettings()
                .onSuccess { settings ->
                    handleEventSettings(settings)
                }
                .onFailure {
                    handleEventSettingsError()
                }
        }
    }

    fun onInformationRequested() {
        viewModelScope.launch {
            updateEvents { copy(openInformation = triggered) }
        }
    }

    fun onLogoutRequested() {
        viewModelScope.launch(Dispatchers.IO) {
            updateState { copy(isLoggingOut = true) }
            logout()
                .onSuccess {
                    updateState { copy(isLoggingOut = false) }
                    updateEvents { copy(openLogin = triggered) }
                }
                .onFailure {
                    updateState {
                        copy(
                            isLoggingOut = false,
                            alertData = AlertData.Default.copy(
                                title = Label.errorDescriptionLogoutFailedText,
                                onDismiss = ::onLogoutAlertDismissed
                            )
                        )
                    }
                }
        }
    }

    fun onTileSelected(tile: InvitationTileUiModel) {
        viewModelScope.launch {
            val intents = tile.intents
            if (intents.isNotEmpty()) {
                updateState { copy(intents = tile.intents) }
                updateEvents { copy(showTileOptions = triggered) }
            } else {
                updateState { copy(intents = emptyList()) }
            }
        }
    }

    fun onTileIntentOptionSelected(intent: IntentUiModel) {
        viewModelScope.launch {
            updateEvents { copy(hideTileOptions = triggered, openIntent = triggered(intent)) }
        }
    }

    fun onInformationOpened() {
        viewModelScope.launch {
            updateEvents { copy(openInformation = consumed) }
        }
    }

    fun onTileOptionsShown() {
        viewModelScope.launch {
            updateEvents { copy(showTileOptions = consumed) }
        }
    }

    fun onTileOptionsHidden() {
        viewModelScope.launch {
            updateEvents { copy(hideTileOptions = consumed) }
        }
    }

    fun onIntentOpened() {
        viewModelScope.launch {
            updateEvents { copy(openIntent = consumed()) }
        }
    }

    fun onLoginOpened() {
        viewModelScope.launch {
            updateEvents { copy(openLogin = consumed) }
        }
    }

    private suspend fun handleEventSettings(settings: EventSettings) {
        val invitationId = settings.invitationId

        if (invitationId != null) {
            observeInvitation(invitationId)
                .collect { handleInvitationResult(it) }
        } else {
            updateState {
                copy(isLoading = false, isEnabled = false)
            }
        }
    }

    private fun handleEventSettingsError() {
        updateState {
            copy(
                isLoading = false,
                alertData = AlertData.Default.copy(
                    title = Label.errorDescriptionEventNotFoundText,
                    onDismiss = ::onEventNotFoundAlertDismissed
                )
            )
        }
    }

    private fun handleInvitationResult(result: Result<Invitation>) {
        result
            .onSuccess { invitation ->
                updateState {
                    copy(isLoading = false, tiles = invitation.tiles.map { it.toUiModel() })
                }
            }
            .onFailure {
                updateState { copy(isLoading = false, errorData = ErrorData.Default) }
            }
    }

    private fun onEventNotFoundAlertDismissed() {
        updateState { copy(alertData = null) }
        updateEvents { copy(openLogin = triggered) }
    }

    private fun onLogoutAlertDismissed() {
        updateState { copy(alertData = null) }
        updateEvents { copy(openLogin = triggered) }
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
        IntentType.INSTAGRAM -> ThemeR.drawable.ic_instagram_primary
        IntentType.GOOGLE_MAPS -> ThemeR.drawable.ic_maps_primary
        IntentType.WWW -> ThemeR.drawable.ic_web_primary
        else -> ThemeR.drawable.ic_web_primary
    }

    private fun String.getIntentTitle(type: IntentType) = when (type) {
        IntentType.INSTAGRAM -> removePrefix(IntentUrlPrefix.INSTAGRAM).split('/').first()
        IntentType.GOOGLE_MAPS -> Label.homeIntentMapsLabel
        else -> Label.homeIntentWebLabel
    }
}

internal data class HomeUiState(
    val isLoading: Boolean,
    val isEnabled: Boolean,
    val isLoggingOut: Boolean,
    val tiles: List<InvitationTileUiModel>,
    val intents: List<IntentUiModel>,
    val errorData: ErrorData?,
    val alertData: AlertData?
) {
    companion object {
        val Default = HomeUiState(
            isLoading = false,
            isEnabled = true,
            isLoggingOut = false,
            tiles = emptyList(),
            intents = emptyList(),
            errorData = null,
            alertData = null
        )
    }
}

internal data class HomeUiEvents(
    val openInformation: StateEvent,
    val openIntent: StateEventWithContent<IntentUiModel>,
    val showTileOptions: StateEvent,
    val hideTileOptions: StateEvent,
    val openLogin: StateEvent
) {
    companion object {
        val Default = HomeUiEvents(
            openInformation = consumed,
            showTileOptions = consumed,
            hideTileOptions = consumed,
            openIntent = consumed(),
            openLogin = consumed
        )
    }
}
