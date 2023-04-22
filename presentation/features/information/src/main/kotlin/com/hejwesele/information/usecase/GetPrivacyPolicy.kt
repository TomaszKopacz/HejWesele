package com.hejwesele.information.usecase

import com.hejwesele.legaldocument.LegalDocumentParser
import com.hejwesele.legals.LegalDocumentsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPrivacyPolicy @Inject constructor(
    private val repository: LegalDocumentsRepository,
    private val parser: LegalDocumentParser
) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        repository
            .getPrivacyPolicy()
            .mapCatching { parser.parseLegalDocument(it) }
    }
}
