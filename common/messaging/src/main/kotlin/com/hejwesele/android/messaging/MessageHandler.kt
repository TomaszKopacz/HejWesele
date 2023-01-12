package com.hejwesele.android.messaging

interface MessageHandler {
    fun onMessageReceived(data: Map<String, String>)
}
