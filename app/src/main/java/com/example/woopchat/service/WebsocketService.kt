package com.example.woopchat.service

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.channels.ReceiveChannel

interface WebsocketService {
    @Send
    fun sendMessage(request: WoopMessage)

    @Receive
    fun observeWebSocketEvent(): ReceiveChannel<WebSocket.Event>

    @Receive
    fun observeMessages(): ReceiveChannel<WoopMessage>
}