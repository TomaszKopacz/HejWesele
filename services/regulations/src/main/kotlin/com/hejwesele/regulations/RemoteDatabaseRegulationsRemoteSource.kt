package com.hejwesele.regulations

import com.hejwesele.regulations.dto.RegulationPointDto
import com.hejwesele.regulations.mappers.mapModel
import com.hejwesele.regulations.model.RegulationPoint
import com.hejwesele.remotedatabase.RemoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDatabaseRegulationsRemoteSource @Inject constructor(
    private val database: RemoteDatabase
) : RegulationsRemoteSource {

    companion object {
        private const val REGULATIONS_PATH = "regulations/"
        private const val TERMS_AND_CONDITIONS_PATH = "T&C/"
    }

    override suspend fun getTermsAndConditions(): Result<List<RegulationPoint>> = withContext(Dispatchers.IO) {
        database.readAll(
            path = REGULATIONS_PATH + TERMS_AND_CONDITIONS_PATH,
            type = RegulationPointDto::class
        ).mapCatching { termsDtoList ->
            termsDtoList.map { dto -> dto.mapModel() }
        }
    }
}
