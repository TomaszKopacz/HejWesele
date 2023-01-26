package com.hejwesele.model.event

import com.hejwesele.model.home.HomeTile

data class Event(
    val id: String,
    val name: String,
    val homeTiles: List<HomeTile>
)
