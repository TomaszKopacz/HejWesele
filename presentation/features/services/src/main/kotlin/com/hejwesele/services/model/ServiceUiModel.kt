package com.hejwesele.services.model

import androidx.annotation.RawRes

internal data class ServiceUiModel(
    val title: String,
    val name: String?,
    val description: String,
    val color: MaterialServiceColor,
    @RawRes val animation: Int
)
