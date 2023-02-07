package com.hejwesele.events.model

data class Event(
    val id: String?,
    val name: String,
    val homeTiles: List<HomeTile>
)
