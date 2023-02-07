package com.hejwesele.events.remote.mappers

import com.hejwesele.events.model.Event
import com.hejwesele.events.model.Events
import com.hejwesele.events.remote.dto.EventDto
import com.hejwesele.events.remote.dto.EventsDto

internal fun EventsDto.toModel() = Events(
    data = data.map { it.toModel() }
)

internal fun EventDto.toModel() = Event(
    id = id,
    name = name ?: "",
    homeTiles = home_tiles
        ?.map { it.toModel() }
        ?: emptyList()
)

internal fun Event.toDto() = EventDto(
    id = id,
    name = name,
    home_tiles = homeTiles.map { it.toDto() }
)
