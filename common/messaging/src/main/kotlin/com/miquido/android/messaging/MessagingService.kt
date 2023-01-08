package com.miquido.android.messaging

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface MessagingServiceEntryPoint {
    fun getTokenHandlers(): Set<@JvmSuppressWildcards TokenHandler>
    fun getMessageHandlers(): Set<@JvmSuppressWildcards MessageHandler>
}

internal class MessagingService : FirebaseMessagingService() {

    private val entryPoint: MessagingServiceEntryPoint by lazy {
        EntryPoints.get(applicationContext, MessagingServiceEntryPoint::class.java)
    }

    override fun onNewToken(token: String) {
        entryPoint.getTokenHandlers().forEach { it.onNewToken(token) }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        entryPoint.getMessageHandlers().forEach { it.onMessageReceived(message.data) }
    }
}
