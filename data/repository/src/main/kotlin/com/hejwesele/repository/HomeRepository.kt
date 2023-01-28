package com.hejwesele.repository

import com.hejwesele.model.DataResult
import com.hejwesele.model.home.HomeTile
import com.hejwesele.repository.datasource.remote.HomeRemoteSource
import javax.inject.Inject

internal class HomeRepository @Inject constructor(
    private val remoteSource: HomeRemoteSource
) : IHomeRepository {

    override suspend fun getHomeTiles(eventId: String): DataResult<List<HomeTile>> {
        return remoteSource.getHomeTiles(eventId)
    }
}
