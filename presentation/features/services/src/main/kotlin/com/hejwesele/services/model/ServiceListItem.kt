package com.hejwesele.services.model

internal sealed interface ServiceListItem {
    class Label(val text: String) : ServiceListItem
    class Tile(val service: ServiceUiModel) : ServiceListItem
}
