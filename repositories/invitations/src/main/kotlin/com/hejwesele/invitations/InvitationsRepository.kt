package com.hejwesele.invitations

import com.hejwesele.invitations.model.Invitation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InvitationsRepository @Inject constructor(
    private val remoteSource: InvitationsRemoteSource
) {

    fun observeInvitation(invitationId: String) = remoteSource.observeInvitation(invitationId)

    suspend fun getInvitation(invitationId: String) = remoteSource.getInvitation(invitationId)

    suspend fun addInvitation(invitation: Invitation) = remoteSource.addInvitation(invitation)
}
