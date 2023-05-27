package com.hejwesele.legals

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LegalDocumentsRepository @Inject constructor(
    private val remoteSource: LegalDocumentsRemoteSource
) {

    suspend fun getTermsAndConditionsDocument() = withContext(Dispatchers.IO) {
        remoteSource.getTermsAndConditions()
    }

    suspend fun getPrivacyPolicy() = withContext(Dispatchers.IO) {
        remoteSource.getPrivacyPolicy()
    }
}
