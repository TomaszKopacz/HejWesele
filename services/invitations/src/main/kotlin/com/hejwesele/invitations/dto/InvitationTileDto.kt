package com.hejwesele.invitations.dto

import androidx.annotation.Keep

@Keep
data class InvitationTileDto(
    var type: String = "wishes",
    var title: String? = null,
    var subtitle: String? = null,
    var description: String? = null,
    var avatars: List<String> = emptyList(),
    var urls: List<String> = emptyList()
)
