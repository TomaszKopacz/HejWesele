package com.hejwesele.invitations

import com.hejwesele.invitations.model.Invitation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InvitationsRepository @Inject constructor(
    private val remoteSource: InvitationsRemoteSource
) {

    suspend fun observeInvitation(invitationId: String) = withContext(Dispatchers.IO) {
        remoteSource.observeInvitation(invitationId)
    }

    suspend fun getInvitation(invitationId: String) = withContext(Dispatchers.IO) {
        remoteSource.getInvitation(invitationId)
    }

    suspend fun addInvitation(invitation: Invitation) = withContext(Dispatchers.IO) {
        remoteSource.addInvitation(invitation)
    }
}
