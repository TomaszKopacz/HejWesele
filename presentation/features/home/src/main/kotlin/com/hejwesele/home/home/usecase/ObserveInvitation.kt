package com.hejwesele.home.home.usecase

import com.hejwesele.invitations.InvitationsRepository
import javax.inject.Inject

internal class ObserveInvitation @Inject constructor(
    private val repository: InvitationsRepository
) {

    suspend operator fun invoke(invitationId: String) = repository.observeInvitation("-NNxXTrIHZwNNP1ZUlWP")
}
