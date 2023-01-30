package com.hejwesele.datastore.dto.event

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName

@Keep
data class EventDto(
    @DocumentId
    val id: String = "",

    @get:PropertyName(value = "home_tiles")
    val homeTiles: List<DocumentReference> = emptyList(),

    @get:PropertyName(value = "name")
    val name: String = ""
)
