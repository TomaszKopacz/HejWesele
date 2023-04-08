package com.hejwesele.legals

import com.hejwesele.remotedatabase.RemoteDatabase
import com.hejwesele.result.extensions.flatMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDatabaseLegalDocumentsRemoteSource @Inject constructor(
    private val database: RemoteDatabase
) : LegalDocumentsRemoteSource {

    companion object {
        private const val LEGAL_DOCUMENTS_PATH = "legal_documents/"
        private const val TERMS_AND_CONDITIONS_ID = "terms_and_conditions"
        private const val PRIVACY_POLICY_ID = "privacy_policy"
    }

    override suspend fun getPrivacyPolicy(): Result<String> = withContext(Dispatchers.IO) {
        database.read(LEGAL_DOCUMENTS_PATH, PRIVACY_POLICY_ID, String::class).flatMap { policies ->
            policies?.let { Result.success(it) } ?: Result.failure(Throwable("Privacy Policy not found."))
        }
    }

    override suspend fun getTermsAndConditions(): Result<String> = withContext(Dispatchers.IO) {
        database.read(LEGAL_DOCUMENTS_PATH, TERMS_AND_CONDITIONS_ID, String::class).flatMap { terms ->
            terms?.let { Result.success(it) } ?: Result.failure(Throwable("Terms and Conditions not found."))
        }
    }
}
