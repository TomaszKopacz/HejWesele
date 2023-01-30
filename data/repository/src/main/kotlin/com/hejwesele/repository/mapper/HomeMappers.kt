package com.hejwesele.repository.mapper

import com.hejwesele.datastore.dto.home.HomeTileDto
import com.hejwesele.model.common.IntentPackage
import com.hejwesele.model.common.IntentType
import com.hejwesele.model.common.IntentUrl
import com.hejwesele.model.common.UrlPrefix
import com.hejwesele.model.home.HomeTile
import com.hejwesele.model.home.HomeTileType

internal fun HomeTileDto.toModel() = HomeTile(
    type = type.toHomeTileTypeModel(),
    title = title,
    subtitle = subtitle,
    description = description,
    photoUrls = photoUrls,
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
