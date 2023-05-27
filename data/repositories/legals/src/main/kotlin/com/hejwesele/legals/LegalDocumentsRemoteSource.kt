package com.hejwesele.legals

interface LegalDocumentsRemoteSource {

    suspend fun getPrivacyPolicy(): Result<String>

    suspend fun getTermsAndConditions(): Result<String>
}
