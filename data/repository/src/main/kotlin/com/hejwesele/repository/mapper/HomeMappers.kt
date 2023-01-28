package com.hejwesele.repository.mapper

import com.hejwesele.model.home.HomeTile
import com.hejwesele.datastore.dto.home.HomeTileDto
import com.hejwesele.model.home.HomeTileType

internal fun HomeTileDto.toModel() = HomeTile(
    type = type.toHomeTileTypeModel(),
    title = title,
    subtitle = subtitle,
    description = description,
    photoUrls = photoUrls
)

internal fun String.toHomeTileTypeModel() =  when (this) {
    "couple" -> HomeTileType.COUPLE
    "date" -> HomeTileType.DATE
    "church" -> HomeTileType.CHURCH
    "venue" -> HomeTileType.VENUE
    "wishes" -> HomeTileType.WISHES
    else -> HomeTileType.WISHES
}
