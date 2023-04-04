package com.hejwesele.regulations.model

data class RegulationPoint(
    val type: RegulationPointType,
    val level: Int,
    val order: String?,
    val text: String
)
