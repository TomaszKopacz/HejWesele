package com.hejwesele.home.model

import androidx.annotation.RawRes
import com.hejwesele.invitations.model.InvitationTileType

internal data class InvitationTileUiModel(
    val type: InvitationTileType,
    val title: String,
    val subtitle: String,
    val description: String,
    val avatars: List<String>,
    @RawRes val animationResId: Int,
    val intents: List<IntentUiModel>,
    val clickable: Boolean
)
