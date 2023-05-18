package com.hejwesele.services

interface IServicesNavigation {
    fun openServiceDetails(title: String, name: String?, description: String)
    fun logout()
}
