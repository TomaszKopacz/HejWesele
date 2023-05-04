package com.hejwesele.qr.model

import androidx.annotation.Keep

@Keep
data class EventCredentials(
    val name: String,
    val password: String
)
