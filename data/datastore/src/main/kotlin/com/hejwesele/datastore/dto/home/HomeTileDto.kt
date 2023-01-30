package com.hejwesele.datastore.dto.home

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

@Keep
data class HomeTileDto(
    @DocumentId
    val id: String = "",

    @get:PropertyName(value = "order")
    @set:PropertyName(value = "order")
    var order: Int = 0,

    @get:PropertyName(value = "type")
    @set:PropertyName(value = "type")
    var type: String = "",

    @get:PropertyName(value = "title")
    @set:PropertyName(value = "title")
    var title: String = "",

    @get:PropertyName(value = "subtitle")
    @set:PropertyName(value = "subtitle")
    var subtitle: String? = null,

    @get:PropertyName(value = "description")
    @set:PropertyName(value = "description")
    var description: String = "",

    @get:PropertyName(value = "photo_urls")
    @set:PropertyName(value = "photo_urls")
    var photoUrls: List<String> = emptyList(),

    @get:PropertyName(value = "urls")
    @set:PropertyName(value = "urls")
    var urls: List<String>? = null
)
