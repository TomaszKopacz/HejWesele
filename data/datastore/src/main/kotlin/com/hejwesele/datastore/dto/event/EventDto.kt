package com.hejwesele.datastore.dto.event

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName

data class EventDto (
    @DocumentId
    val id: String = "",

    @get:PropertyName(value = "home_tiles")
    val homeTiles: List<DocumentReference> = emptyList(),

    @get:PropertyName(value = "name")
    val name: String = ""
)
