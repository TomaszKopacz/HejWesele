package com.hejwesele.regulations.mappers

import com.hejwesele.regulations.dto.RegulationPointDto
import com.hejwesele.regulations.model.RegulationPoint
import com.hejwesele.regulations.model.RegulationPointType

internal fun RegulationPointDto.mapModel() = RegulationPoint(
    type = type.mapRegulationPointTypeModel(),
    level = level,
    order = order,
    text = text
)

private fun String.mapRegulationPointTypeModel(): RegulationPointType {
    return when (this) {
        "title" -> RegulationPointType.TITLE
        "text" -> RegulationPointType.TEXT
        "paragraph" -> RegulationPointType.PARAGRAPH
        "number_point" -> RegulationPointType.NUMBER_POINT
        "letter_point" -> RegulationPointType.LETTER_POINT
        "bullet_point" -> RegulationPointType.BULLET_POINT
        else -> RegulationPointType.TEXT
    }
}
