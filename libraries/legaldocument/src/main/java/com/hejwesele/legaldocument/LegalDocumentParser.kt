package com.hejwesele.legaldocument

import com.hejwesele.legaldocument.LegalDocumentDelimiters.LEGAL_POINT_DELIMITER
import com.hejwesele.legaldocument.LegalDocumentDelimiters.LEGAL_POINT_TAG_DELIMITER
import javax.inject.Inject

class LegalDocumentParser @Inject constructor() {

    fun parseLegalDocument(document: String): List<LegalPoint> =
        document
            .split(LEGAL_POINT_DELIMITER)
            .map { entry -> entry.trim() }
            .map { entry ->
                entry
                    .split(LEGAL_POINT_TAG_DELIMITER)
                    .parseEntry()
            }

    private fun List<String>.parseEntry(): LegalPoint {
        val text = findTagValue(LegalPointTag.Text)
        val type = findTagValue(LegalPointTag.Type).toLegalPointType()
        val level = findTagValue(LegalPointTag.Level)
        val order = findTagValue(LegalPointTag.Order).toLegalPointOrder()

        return LegalPoint(
            text = text,
            type = type,
            level = level,
            order = order
        )
    }

    private fun List<String>.findTagValue(tag: LegalPointTag): String =
        find {it.startsWith(tag.key) }?.removePrefix("${tag.key}=").orEmpty()

    private fun String.toLegalPointType() = when (this) {
        "title" -> LegalPointType.TITLE
        "text" -> LegalPointType.TEXT
        "paragraph" -> LegalPointType.PARAGRAPH
        "number_point" -> LegalPointType.NUMBER_POINT
        "letter_point" -> LegalPointType.LETTER_POINT
        "bullet_point" -> LegalPointType.BULLET_POINT
        else -> LegalPointType.TEXT
    }

    private fun String.toLegalPointOrder() =
        if (this.isEmpty() || this == "null") null else this
}