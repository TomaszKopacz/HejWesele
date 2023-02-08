package com.hejwesele.home.usecase

import com.hejwesele.invitations.InvitationsRepository
import javax.inject.Inject

class GetInvitation @Inject constructor(
    private val repository: InvitationsRepository
) {

    suspend operator fun invoke(invitationId: String) = repository.getInvitation("-NNxXTrIHZwNNP1ZUlWP")
}
