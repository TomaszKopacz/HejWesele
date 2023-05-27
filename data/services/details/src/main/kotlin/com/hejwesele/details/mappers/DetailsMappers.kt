package com.hejwesele.details.mappers

import com.hejwesele.details.dto.DetailsDto
import com.hejwesele.details.model.Details
import kotlinx.datetime.toLocalDateTime

internal fun DetailsDto.mapModel() = Details(
    date = date?.toLocalDateTime() ?: throw IllegalArgumentException("Required details date is not present.")
)

internal fun Details.mapDto() = DetailsDto(
    date = date.toString()
)
