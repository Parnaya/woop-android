package com.example.woopchat.service

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.channels.ReceiveChannel

//data class Request(
//    val id: String,
//    val createdAt: String,
//    val messages: List<WoopMessage>,
//)
//
//data class WoopMessage(
//    val type: String,
//    val data: JsonElement,
//)
//
//data class Entity(
//    val id: String,
//    val data: String,
//    val tags: List<String>,
//)

interface WebsocketService {
    @Send
    fun sendMessage(request: WoopMessage)

    @Receive
    fun observeWebSocketEvent(): ReceiveChannel<WebSocket.Event>

    @Receive
    fun observeMessages(): ReceiveChannel<WoopMessage>
}