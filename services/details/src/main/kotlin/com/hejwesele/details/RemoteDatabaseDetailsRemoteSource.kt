package com.hejwesele.details

import com.hejwesele.details.dto.DetailsDto
import com.hejwesele.details.mappers.mapDto
import com.hejwesele.details.mappers.mapModel
import com.hejwesele.details.model.Details
import com.hejwesele.remotedatabase.RemoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDatabaseDetailsRemoteSource @Inject constructor(
    private val database: RemoteDatabase
) : DetailsRemoteSource {

    companion object {
        private const val DETAILS_PATH = "details/"
    }

    override suspend fun observeDetails(detailsId: String): Flow<Result<Details>> = withContext(Dispatchers.IO) {
        database.observe(
            path = DETAILS_PATH,
            id = detailsId,
            type = DetailsDto::class
        ).map { result ->
            result.mapCatching { dto -> dto.mapModel() }
        }
    }

    override suspend fun addDetails(details: Details): Result<Details> = withContext(Dispatchers.IO) {
        database.write(
            path = DETAILS_PATH,
            item = details.mapDto()
        ).map { details }
    }
}
