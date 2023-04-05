package com.hejwesele.regulations

import com.hejwesele.regulations.model.RegulationPoint

interface RegulationsRemoteSource {

    suspend fun getTermsAndConditions(): Result<List<RegulationPoint>>

    suspend fun getPrivacyPolicy(): Result<List<RegulationPoint>>
}
