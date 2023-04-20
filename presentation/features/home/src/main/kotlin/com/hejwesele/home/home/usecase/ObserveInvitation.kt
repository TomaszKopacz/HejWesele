package com.hejwesele.home.home.usecase

import com.hejwesele.invitations.InvitationsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ObserveInvitation @Inject constructor(
    private val repository: InvitationsRepository
) {

    suspend operator fun invoke(invitationId: String) = withContext(Dispatchers.IO) {
        repository.observeInvitation(invitationId)
    }
}
