package com.hejwesele.regulations

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegulationsRepository @Inject constructor(
    private val remoteSource: RegulationsRemoteSource
) {

    suspend fun getTermsAndConditions() = withContext(Dispatchers.IO) {
        remoteSource.getTermsAndConditions()
    }
}
