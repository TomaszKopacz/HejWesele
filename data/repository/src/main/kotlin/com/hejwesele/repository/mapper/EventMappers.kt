package com.hejwesele.repository.mapper

import com.hejwesele.datastore.dto.event.EventDto
import com.hejwesele.model.event.Event

internal fun EventDto.toModel() = Event(
    id = id,
    name = name,
    homeTiles = emptyList()
)
