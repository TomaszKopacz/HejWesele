package com.miquido.android.messaging

interface MessageHandler {
    fun onMessageReceived(data: Map<String, String>)
}
