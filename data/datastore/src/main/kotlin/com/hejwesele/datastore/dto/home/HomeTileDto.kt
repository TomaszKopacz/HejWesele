package com.hejwesele.datastore.dto.home

import com.google.firebase.firestore.PropertyName

data class HomeTileDto (
    @get:PropertyName(value = "order")
    @set:PropertyName(value = "order")
    var order: Int = 0,

    @get:PropertyName(value = "title")
    @set:PropertyName(value = "title")
    var title: String = "",

    @get:PropertyName(value = "description")
    @set:PropertyName(value = "description")
    var description: String = ""
)
