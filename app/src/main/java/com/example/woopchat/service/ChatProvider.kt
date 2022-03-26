package com.example.woopchat.service

import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.WebSocket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class ChatProvider(
    private val lifecycle: Lifecycle,
    private val scope: CoroutineScope,
    private val serviceProvider: ServiceProvider,
) {

    private val updateFiltersChannel: Channel<WoopMessage> = Channel()
    private val service = serviceProvider.getService(lifecycle)

    init {
        scope.launch(Dispatchers.IO) {
            val channel = service.observeWebSocketEvent()
            while (true) {
                val state = channel.receive()
                var job: Job? = null

                when (state) {
                    is WebSocket.Event.OnConnectionOpened<*> -> job = scope.launch {
                        for (msg in updateFiltersChannel) {
                            service.sendMessage(msg)
                        }
                    }
                    is WebSocket.Event.OnConnectionFailed,
                    is WebSocket.Event.OnConnectionClosing,
                    is WebSocket.Event.OnConnectionClosed -> {
                        job?.cancel()
                        job = null
                    }
                    is WebSocket.Event.OnMessageReceived -> {}
                }
            }
        }
    }

    suspend fun onReceiveMessage(action: suspend (Entity) -> Unit) {
        service
            .observeEntities()
            .consumeAsFlow()
            .collect {
                action.invoke(it)
            }
    }

    suspend fun updateFilters(filters: List<String>) {
        updateFiltersChannel.send(filters.revert())
    }

    suspend fun sendMessage(entity: Entity) {
        service.sendMessage(
            entity.revert()
        )
    }

    private fun List<String>.revert() = WoopMessage(
        filters = this,
    )

    private fun Entity.revert() = WoopMessage(
        entity_create = this,
    )
}