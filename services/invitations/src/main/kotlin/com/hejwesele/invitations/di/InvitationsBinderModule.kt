package com.hejwesele.invitations.di

import com.hejwesele.invitations.FirebaseInvitationsRemoteSource
import com.hejwesele.invitations.InvitationsRemoteSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface InvitationsBinderModule {

    @Binds
    fun bindInvitationsRemoteSource(impl: FirebaseInvitationsRemoteSource): InvitationsRemoteSource
}