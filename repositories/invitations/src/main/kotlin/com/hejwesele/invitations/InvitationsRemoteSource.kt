package com.hejwesele.invitations

import com.hejwesele.invitations.model.Invitation
import com.hejwesele.result.CompletableResult
import com.hejwesele.result.Result
import kotlinx.coroutines.flow.Flow

interface InvitationsRemoteSource {

    fun observeInvitation(invitationId: String): Flow<Result<Invitation>>

    suspend fun getInvitation(invitationId: String): Result<Invitation>

    suspend fun addInvitation(invitation: Invitation): CompletableResult
}
