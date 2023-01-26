package com.hejwesele.repository.mapper

import com.hejwesele.datastore.DatastoreResult
import com.hejwesele.datastore.DatastoreResult.Failure
import com.hejwesele.datastore.DatastoreResult.Success
import com.hejwesele.model.DataResult
import com.hejwesele.model.GeneralError
import com.hejwesele.model.failure
import com.hejwesele.model.success

internal fun <DATA : Any> DatastoreResult<DATA>.toDataResult(): DataResult<DATA> {
    return when (this) {
        is Success -> success(value)
        is Failure -> failure(GeneralError.DatastoreError(exception.message))
    }
}
