package com.hejwesele.services

interface IServicesNavigation {
    fun openServiceDetails(serviceId: String)

    fun navigateUp()

    fun logout()
}
