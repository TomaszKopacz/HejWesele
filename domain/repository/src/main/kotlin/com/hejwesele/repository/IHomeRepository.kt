package com.hejwesele.repository

import com.hejwesele.model.DataResult
import com.hejwesele.model.home.HomeTile

interface IHomeRepository {

    suspend fun getHomeTiles(eventId: String): DataResult<List<HomeTile>>
}
