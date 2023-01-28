package com.hejwesele.repository.datasource.remote

import com.hejwesele.datastore.Datastore
import com.hejwesele.datastore.mapSuccess
import com.hejwesele.model.DataResult
import com.hejwesele.model.home.HomeTile
import com.hejwesele.repository.mapper.toDataResult
import com.hejwesele.repository.mapper.toModel
import javax.inject.Inject

internal class HomeRemoteSource @Inject constructor(
    private val datastore: Datastore
) {

    suspend fun getHomeTiles(eventId: String): DataResult<List<HomeTile>> {
        return datastore.getHomeTiles(eventId)
            .mapSuccess { tilesList ->
                tilesList
                    .sortedBy { dto -> dto.order }
                    .map { dto -> dto.toModel() }
            }
            .toDataResult()
    }
}
