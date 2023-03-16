package com.hejwesele.invitations.model

import com.hejwesele.intent.IntentData

data class InvitationTile(
    val type: InvitationTileType,
    val title: String,
    val subtitle: String? = null,
    val description: String? = null,
    val avatars: List<String>,
    val intents: List<IntentData>
)
