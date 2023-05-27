package com.hejwesele.invitations

import com.hejwesele.invitations.dto.InvitationDto
import com.hejwesele.invitations.mappers.mapDto
import com.hejwesele.invitations.mappers.mapModel
import com.hejwesele.invitations.model.Invitation
import com.hejwesele.remotedatabase.RemoteDatabase
import com.hejwesele.result.notFound
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDatabaseInvitationsRemoteSource @Inject constructor(
    private val database: RemoteDatabase
) : InvitationsRemoteSource {

    companion object {
        private const val INVITATIONS_PATH = "invitations/"
    }

    override suspend fun observeInvitation(invitationId: String): Flow<Result<Invitation>> = withContext(Dispatchers.IO) {
        database.observe(
            path = INVITATIONS_PATH,
            id = invitationId,
            type = InvitationDto::class
        ).map { result ->
            result.mapCatching { dto -> dto.mapModel() }
        }
    }

    override suspend fun getInvitation(invitationId: String): Result<Invitation> = withContext(Dispatchers.IO) {
        database.read(
            path = INVITATIONS_PATH,
            id = invitationId,
            type = InvitationDto::class
        ).mapCatching { dto ->
            dto?.mapModel() ?: throw notFound(name = "invitation", id = invitationId)
        }
    }

    override suspend fun addInvitation(invitation: Invitation): Result<Invitation> = withContext(Dispatchers.IO) {
        database.write(
            path = INVITATIONS_PATH,
            item = invitation.mapDto()
        ).map { invitation }
    }
}
