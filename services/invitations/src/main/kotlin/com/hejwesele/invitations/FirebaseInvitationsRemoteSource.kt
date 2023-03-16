package com.hejwesele.invitations

import com.hejwesele.invitations.dto.InvitationDto
import com.hejwesele.invitations.mappers.mapDto
import com.hejwesele.invitations.mappers.mapModel
import com.hejwesele.invitations.model.Invitation
import com.hejwesele.realtimedatabase.FirebaseRealtimeDatabase
import com.hejwesele.result.notFound
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseInvitationsRemoteSource @Inject constructor(
    private val database: FirebaseRealtimeDatabase
) : InvitationsRemoteSource {

    companion object {
        private const val INVITATIONS_PATH = "invitations/"
    }

    override fun observeInvitation(invitationId: String): Flow<Result<Invitation>> {
        return database.observe(
            path = INVITATIONS_PATH,
            id = invitationId,
            type = InvitationDto::class
        ).map { result ->
            result.mapCatching { dto -> dto.mapModel() }
        }
    }

    override suspend fun getInvitation(invitationId: String): Result<Invitation> {
        return database.read(
            path = INVITATIONS_PATH,
            id = invitationId,
            type = InvitationDto::class
        ).mapCatching { dto ->
            dto?.mapModel() ?: throw notFound(name = "invitation", id = invitationId)
        }
    }

    override suspend fun addInvitation(invitation: Invitation): Result<Invitation> {
        return database.write(
            path = INVITATIONS_PATH,
            item = invitation.mapDto()
        ).map { invitation }
    }
}
