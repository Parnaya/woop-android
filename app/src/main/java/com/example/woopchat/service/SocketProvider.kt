package com.example.woopchat.service

import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.WebSocket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.launch
import org.threeten.bp.OffsetDateTime

class ChatUseCases(
    private val userTag: String,
    private val chatTag: String,
    private val socketUseCases: SocketUseCases,
) {

    private var lastPageKey: OffsetDateTime? = null
        @Synchronized get
        @Synchronized set

    private val cache = MessagesCollection<String, Entity> { it.id }

    suspend fun onUpdates(action: suspend (List<Entity>) -> Unit) {
        socketUseCases.entitiesFlow
            .filter { it.entities.first().tags.contains(chatTag) }
            .collect { msg ->
                if (cache.isEmpty() || cache.last().createdAt > msg.entities.first().createdAt) { //new messages
                    cache.putAll(0, msg.entities)
                } else { //new page
                    cache.putAll(cache.size, msg.entities)
                }
                action.invoke(cache)
            }
    }

    suspend fun sendMessage(entity: Entity) {
        cache.put(entity)
        socketUseCases.sendMessage(entity)
    }

    suspend fun loadMessages() {
        socketUseCases.fetch(last = cache.first().createdAt)
    }

    fun ensurePagination(): Boolean {
        if (lastPageKey != null) {
            if (cache.first().createdAt == lastPageKey) return false
        }
        lastPageKey = cache.first().createdAt
        return true
    }

    companion object {
        const val MessageType = "type|message"
    }
}

class SocketUseCases(
    private val lifecycle: Lifecycle,
    private val scope: CoroutineScope,
    private val websocketServiceProvider: WebsocketServiceProvider,
) {

    private val updateFiltersChannel: Channel<WoopMessage> = Channel()
    private val service = websocketServiceProvider.getService(lifecycle)

    val entitiesFlow = service
        .observeMessages()
        .receiveAsFlow()
        .filterIsInstance<WoopMessage.Entities>()
        .shareIn(scope, Lazily)

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
                            fetch()
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

    suspend fun updateFilters(filters: List<String>) {
        updateFiltersChannel.send(filters.revert())
    }

    suspend fun fetch(last: OffsetDateTime = OffsetDateTime.now(), size: Int = 50) {
        service.sendMessage(
            WoopMessage.Fetch(last, size)
        )
    }

    suspend fun sendMessage(entity: Entity) {
        service.sendMessage(
            entity.revert()
        )
    }

    private fun List<String>.revert() = WoopMessage.Filter(
        filter = joinToString("&&"),
    )

    private fun Entity.revert() = WoopMessage.Entities(
        entities = listOf(this),
    )
}