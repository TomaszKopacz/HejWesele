package com.hejwesele.regulations.dto

import androidx.annotation.Keep

@Keep
data class RegulationPointDto(
    var type: String = "text",
    var level: Int = 0,
    var order: String? = null,
    var text: String = ""
)
