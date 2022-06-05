package com.example.woopchat.service

import com.example.woopchat.coroutines.CoroutineScopes
import com.example.woopchat.coroutines.Dispatchers
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.WebSocket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

interface IChatUseCases {
    suspend fun onUpdates(action: suspend (List<Entity>) -> Unit)
    suspend fun sendMessage(entity: Entity)
    suspend fun loadMessages()
    suspend fun ensurePagination(): Boolean
}

class ChatUseCases(
    private val userTag: String,
    private val chatTag: String,
    private val socketUseCases: ISocketUseCases,
): IChatUseCases {

    private var mutex = Mutex()
    private var lastPageKey: OffsetDateTime? = null

    private val cache = MessagesCollection<String, Entity> { it.id }

    override suspend fun onUpdates(action: suspend (List<Entity>) -> Unit) {
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

    override suspend fun sendMessage(entity: Entity) {
        cache.put(entity)
        socketUseCases.sendMessage(entity)
    }

    override suspend fun loadMessages() {
        socketUseCases.fetch(last = cache.first().createdAt)
    }

    override suspend fun ensurePagination(): Boolean {
        mutex.withLock {
            if (lastPageKey != null) {
                if (cache.first().createdAt == lastPageKey) return false
            }
            lastPageKey = cache.first().createdAt
        }
        return true
    }

    companion object {
        const val MessageType = "type|message"
    }
}

interface ISocketUseCases {
    val entitiesFlow: SharedFlow<WoopMessage.Entities>

    suspend fun updateFilters(filters: List<String>)
    suspend fun fetch(last: OffsetDateTime = OffsetDateTime.now(), size: Int = 50)
    suspend fun sendMessage(entity: Entity)
}

class SocketUseCases @Inject constructor(
    private val service: WebsocketService,
) : ISocketUseCases {

    private val updateFiltersChannel: Channel<WoopMessage> = Channel()

    override val entitiesFlow = service
        .observeMessages()
        .receiveAsFlow()
        .filterIsInstance<WoopMessage.Entities>()
        .shareIn(CoroutineScopes.io, Lazily)

    init {
        CoroutineScopes.io.launch {
            val channel = service.observeWebSocketEvent()
            while (true) {
                val state = channel.receive()
                var job: Job? = null

                when (state) {
                    is WebSocket.Event.OnConnectionOpened<*> -> job = CoroutineScopes.io.launch {
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

    override suspend fun updateFilters(filters: List<String>) {
        updateFiltersChannel.send(filters.revert())
    }

    override suspend fun fetch(last: OffsetDateTime, size: Int) {
        service.sendMessage(
            WoopMessage.Fetch(last, size)
        )
    }

    override suspend fun sendMessage(entity: Entity) {
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