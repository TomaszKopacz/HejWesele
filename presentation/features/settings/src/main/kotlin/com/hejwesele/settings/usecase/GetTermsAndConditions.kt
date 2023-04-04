package com.hejwesele.settings.usecase

import com.hejwesele.regulations.RegulationsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetTermsAndConditions @Inject constructor(
    private val repository: RegulationsRepository
) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        repository.getTermsAndConditions()
    }
}
