package com.example.woopchat.service

import com.example.woopchat.proto_model.WoopMessage
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.channels.ReceiveChannel

interface WebsocketService {
    @Send
    fun sendMessage(message: WoopMessage)

    @Receive
    fun observeWebSocketEvent(): ReceiveChannel<WebSocket.Event>

    @Receive
    fun observeEntities(): ReceiveChannel<com.example.woopchat.proto_model.Entity>
}