package com.hejwesele.services.mappers

import com.hejwesele.intent.mapIntentUrlModel
import com.hejwesele.services.dto.ServiceDetailsDto
import com.hejwesele.services.dto.ServiceDto
import com.hejwesele.services.dto.ServicesDto
import com.hejwesele.services.model.Service
import com.hejwesele.services.model.ServiceDetails
import com.hejwesele.services.model.ServiceType
import com.hejwesele.services.model.ServiceType.DRINK
import com.hejwesele.services.model.ServiceType.FOOD
import com.hejwesele.services.model.ServiceType.INSTAX
import com.hejwesele.services.model.ServiceType.MOVIE
import com.hejwesele.services.model.ServiceType.MUSIC
import com.hejwesele.services.model.ServiceType.PHOTO
import com.hejwesele.services.model.ServiceType.VENUE
import com.hejwesele.services.model.Services

internal fun ServicesDto.mapModel() = Services(
    attractions = attractions.map { it.mapModel() },
    partners = partners.map { it.mapModel() }
)

internal fun Services.mapDto() = ServicesDto(
    attractions = attractions.map { it.mapDto() },
    partners = partners.map { it.mapDto() }
)

internal fun ServiceDto.mapModel() = Service(
    id = id ?: throw IllegalArgumentException("Required service id is not present."),
    type = type.mapServiceTypeModel(),
    title = title ?: throw IllegalArgumentException("Required service title is not present."),
    name = name,
    description = description ?: throw IllegalArgumentException("Required service description is not present."),
    details = details.map { it.mapModel() },
    image = image,
    intents = urls.map { it.mapIntentUrlModel() }
)

internal fun Service.mapDto() = ServiceDto(
    id = id,
    type = type.mapServiceTypeString(),
    title = title,
    name = name,
    description = description,
    details = details.map { it.mapDto() },
    image = image,
    urls = intents.map { it.intentUrl }
)

internal fun ServiceDetailsDto.mapModel() = ServiceDetails(
    title = title ?: throw IllegalArgumentException("Required service details title is not present."),
    content = content
)

internal fun ServiceDetails.mapDto() = ServiceDetailsDto(
    title = title,
    content = content
)

private fun String.mapServiceTypeModel() = when (this) {
    "venue" -> VENUE
    "music" -> MUSIC
    "photo" -> PHOTO
    "movie" -> MOVIE
    "food" -> FOOD
    "drink" -> DRINK
    "instax" -> INSTAX
    else -> DRINK
}

private fun ServiceType.mapServiceTypeString() = when (this) {
    VENUE -> "venue"
    MUSIC -> "music"
    PHOTO -> "photo"
    MOVIE -> "movie"
    FOOD -> "food"
    DRINK -> "drink"
    INSTAX -> "instax"
    else -> "drink"
}
