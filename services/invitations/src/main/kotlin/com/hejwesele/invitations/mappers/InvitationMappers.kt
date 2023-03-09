package com.hejwesele.invitations.mappers

import com.hejwesele.invitations.dto.InvitationDto
import com.hejwesele.invitations.dto.InvitationTileDto
import com.hejwesele.invitations.model.IntentPackage
import com.hejwesele.invitations.model.IntentType
import com.hejwesele.invitations.model.IntentUrl
import com.hejwesele.invitations.model.IntentUrlPrefix
import com.hejwesele.invitations.model.Invitation
import com.hejwesele.invitations.model.InvitationTile
import com.hejwesele.invitations.model.InvitationTileType

internal fun InvitationDto.mapModel() = Invitation(
    id = id ?: throw IllegalArgumentException("Invitation ID is not present"),
    tiles = tiles.map { it.mapModel() }
)

internal fun Invitation.mapDto(): InvitationDto {
    return InvitationDto(
        id = id,
        tiles = tiles.map { it.mapDto() }
    )
}

internal fun InvitationTile.mapDto(): InvitationTileDto {
    return InvitationTileDto(
        type = type.mapToString(),
        title = title,
        subtitle = subtitle,
        description = description,
        avatars = avatars,
        urls = intents.map { it.url }
    )
}

private fun InvitationTileDto.mapModel(): InvitationTile {
    return InvitationTile(
        type = type.mapInvitationTileTypeModel(),
        title = title ?: throw IllegalArgumentException("Required invitation title is not present"),
        subtitle = subtitle,
        description = description,
        avatars = avatars,
        intents = urls.map { url -> url.mapIntentUrlModel() }
    )
}

private fun String.mapInvitationTileTypeModel(): InvitationTileType {
    return when (this) {
        "couple" -> InvitationTileType.COUPLE
        "date" -> InvitationTileType.DATE
        "church" -> InvitationTileType.CHURCH
        "venue" -> InvitationTileType.VENUE
        "wishes" -> InvitationTileType.WISHES
        else -> InvitationTileType.WISHES
    }
}

private fun String.mapIntentUrlModel(): IntentUrl {
    return when {
        startsWith(IntentUrlPrefix.INSTAGRAM) -> IntentUrl(
            type = IntentType.INSTAGRAM,
            intentPackage = IntentPackage.instagram,
            url = this
        )
        startsWith(IntentUrlPrefix.GOOGLE_MAPS) -> IntentUrl(
            type = IntentType.GOOGLE_MAPS,
            intentPackage = IntentPackage.google_maps,
            url = this
        )
        else -> {
            IntentUrl(
                type = IntentType.WWW,
                intentPackage = null,
                url = this
            )
        }
    }
}

private fun InvitationTileType.mapToString(): String {
    return when (this) {
        InvitationTileType.COUPLE -> "couple"
        InvitationTileType.DATE -> "date"
        InvitationTileType.CHURCH -> "church"
        InvitationTileType.VENUE -> "venue"
        InvitationTileType.WISHES -> "wishes"
        else -> "wishes"
    }
}
