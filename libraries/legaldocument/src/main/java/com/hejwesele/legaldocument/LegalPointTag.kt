package com.hejwesele.legaldocument

internal sealed class LegalPointTag(val key: String) {
    object Text : LegalPointTag("text")
    object Type : LegalPointTag("type")
    object Level : LegalPointTag("level")
    object Order : LegalPointTag("order")
}
