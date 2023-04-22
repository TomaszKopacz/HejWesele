package com.hejwesele.details

import com.hejwesele.details.model.Details
import kotlinx.coroutines.flow.Flow

interface DetailsRemoteSource {

    suspend fun observeDetails(detailsId: String): Flow<Result<Details>>

    suspend fun addDetails(details: Details): Result<Details>
}
