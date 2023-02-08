package com.hejwesele.invitations

import com.hejwesele.invitations.dto.InvitationDto
import com.hejwesele.invitations.mappers.mapDto
import com.hejwesele.invitations.mappers.safeMapModel
import com.hejwesele.invitations.model.Invitation
import com.hejwesele.realtimedatabase.FirebaseRealtimeDatabase
import com.hejwesele.realtimedatabase.FirebaseResult.Error
import com.hejwesele.realtimedatabase.FirebaseResult.NoSuchItem
import com.hejwesele.realtimedatabase.FirebaseResult.Success
import com.hejwesele.result.CompletableResult
import com.hejwesele.result.Result
import com.hejwesele.result.completed
import com.hejwesele.result.failed
import com.hejwesele.result.failure
import com.hejwesele.result.flatMapSuccess
import com.hejwesele.result.serviceError
import com.hejwesele.result.success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

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
        ).map { firebaseResult ->
            when (firebaseResult) {
                is Success -> success(firebaseResult.value)
                is Error -> failure(serviceError(firebaseResult.exception))
                is NoSuchItem -> failure(serviceError())
            }
        }.map { result ->
            result.flatMapSuccess {
                it.safeMapModel()
            }
        }
    }

    override suspend fun getInvitation(invitationId: String): Result<Invitation> {
        val result = database.read(
            path = INVITATIONS_PATH,
            id = invitationId,
            type = InvitationDto::class
        )

        return when (result) {
            is Success -> success(result.value)
            is Error -> failure(serviceError(result.exception))
            is NoSuchItem -> failure(serviceError())
        }.flatMapSuccess {
            it.safeMapModel()
        }
    }

    override suspend fun addInvitation(invitation: Invitation): CompletableResult {
        val tileSaved = database.write(
            path = INVITATIONS_PATH,
            item = invitation.mapDto()
        )

        return if (tileSaved) completed() else failed(serviceError())
    }
}
