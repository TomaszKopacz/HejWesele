package com.hejwesele.android.messaging

import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class FirebaseMessaging @Inject constructor() : Messaging {

    private val messaging by lazy { Firebase.messaging }

    override suspend fun getToken(): String {
        return messaging.token.await()
    }

    override fun subscribeToTopic(topic: String) {
        messaging.subscribeToTopic(topic)
    }

    override fun unsubscribeFromTopic(topic: String) {
        messaging.unsubscribeFromTopic(topic)
    }
}
