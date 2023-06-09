package com.hejwesele.android.messaging

interface Messaging {
    suspend fun getToken(): String
    fun subscribeToTopic(topic: String)
    fun unsubscribeFromTopic(topic: String)
}
