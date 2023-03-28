package com.hejwesele.usecase

import com.hejwesele.json.JsonParser
import com.hejwesele.qr.model.EventCredentials
import com.hejwesele.result.extensions.flatMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

class ParseEventQr @Inject constructor(
    private val jsonParser: JsonParser
) {

    suspend operator fun invoke(qrRecord: String): Result<EventCredentials> = withContext(Dispatchers.Default) {
        jsonParser.fromJson<EventCredentials>(qrRecord, EventCredentials::class.java)
            .flatMap { credentials ->
                if (credentials != null) {
                    success(credentials)
                } else {
                    failure(IllegalArgumentException("QR record is not correct $qrRecord"))
                }
            }
    }
}
