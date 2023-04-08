package com.hejwesele.legaldocument

data class LegalPoint(
    val text: String,
    val type: LegalPointType,
    val level: String,
    val order: String?
)
