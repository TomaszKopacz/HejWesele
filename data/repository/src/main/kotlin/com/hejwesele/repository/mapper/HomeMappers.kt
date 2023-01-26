package com.hejwesele.repository.mapper

import com.hejwesele.model.home.HomeTile
import com.hejwesele.datastore.dto.home.HomeTileDto

internal fun HomeTileDto.toModel() = HomeTile(
    order = order,
    title = title,
    description = description
)
