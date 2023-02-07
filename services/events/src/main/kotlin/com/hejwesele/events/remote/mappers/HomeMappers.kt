package com.hejwesele.events.remote.mappers

import com.hejwesele.events.model.HomeTile
import com.hejwesele.events.model.HomeTileType
import com.hejwesele.events.model.IntentPackage
import com.hejwesele.events.model.IntentType
import com.hejwesele.events.model.IntentUrl
import com.hejwesele.events.model.UrlPrefix
import com.hejwesele.events.remote.dto.HomeTileDto

internal fun HomeTileDto.toModel() = HomeTile(
    type = type.toHomeTileTypeModel(),
    title = title,
    subtitle = subtitle,
    description = description,
    photoUrls = photo_urls,
    intents = urls?.map { url -> url.toIntentUrlModel() }
)

internal fun String.toHomeTileTypeModel() = when (this) {
    "couple" -> HomeTileType.COUPLE
    "date" -> HomeTileType.DATE
    "church" -> HomeTileType.CHURCH
    "venue" -> HomeTileType.VENUE
    "wishes" -> HomeTileType.WISHES
    else -> HomeTileType.WISHES
}

internal fun String.toIntentUrlModel(): IntentUrl {
    return when {
        startsWith(UrlPrefix.INSTAGRAM) -> IntentUrl(
            type = IntentType.INSTAGRAM,
            intentPackage = IntentPackage.instagram,
            url = this
        )
        startsWith(UrlPrefix.GOOGLE_MAPS) -> IntentUrl(
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

internal fun HomeTile.toDto() = HomeTileDto(
    type = type.mapToString(),
    title = title,
    subtitle = subtitle,
    description = description,
    photo_urls = photoUrls,
    urls = intents?.map { it.url }
)

internal fun HomeTileType.mapToString() = when (this) {
    HomeTileType.COUPLE -> "couple"
    HomeTileType.DATE -> "date"
    HomeTileType.CHURCH -> "church"
    HomeTileType.VENUE -> "venue"
    HomeTileType.WISHES -> "wishes"
    else -> "wishes"
}
