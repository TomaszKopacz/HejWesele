package com.hejwesele.details

import com.hejwesele.details.model.Details
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailsRepository @Inject constructor(
    private val remoteSource: DetailsRemoteSource
) {
    suspend fun observeDetails(detailsId: String) = withContext(Dispatchers.IO) {
        remoteSource.observeDetails(detailsId)
    }

    suspend fun addDetails(details: Details) = withContext(Dispatchers.IO) {
        remoteSource.addDetails(details)
    }
}
