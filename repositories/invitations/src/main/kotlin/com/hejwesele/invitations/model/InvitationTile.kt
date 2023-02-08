package com.hejwesele.invitations.model

data class InvitationTile(
    val type: InvitationTileType,
    val title: String,
    val subtitle: String? = null,
    val description: String? = null,
    val avatars: List<String>,
    val intents: List<IntentUrl>
)
