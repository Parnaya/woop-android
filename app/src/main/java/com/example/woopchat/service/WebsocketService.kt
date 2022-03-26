package com.example.woopchat.service

import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.channels.ReceiveChannel

data class WoopMessage(
    val filters: List<String>? = null,
    val entity_create: Entity? = null,
)

data class Entity(
    val id: String,
    val data: String,
    val tags: List<String>,
)


interface WebsocketService {
    @Send
    fun sendMessage(message: WoopMessage)

    @Receive
    fun observeWebSocketEvent(): ReceiveChannel<WebSocket.Event>

    @Receive
    fun observeEntities(): ReceiveChannel<Entity>
}