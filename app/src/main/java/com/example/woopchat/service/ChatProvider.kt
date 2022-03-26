package com.example.woopchat.service

import com.example.woopchat.base.Generator
import com.example.woopchat.proto_model.*
import com.google.protobuf.ByteString
import com.google.protobuf.struct
import com.google.protobuf.timestamp
import com.google.protobuf.value
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.WebSocket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import org.threeten.bp.Instant
import com.example.woopchat.proto_model.Entity as ProtoEntity
import com.example.woopchat.proto_model.Tag as ProtoTag

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
                action.invoke(it.convert())
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

    private fun ProtoEntity.convert() = Entity(
        id = id.toStringUtf8(),
        data = data.getFieldsOrThrow("data").stringValue,
        tags = tagsList.map { it.convert() },
    )

    private fun ProtoTag.convert(): String {
        return data.getFieldsOrThrow("data").stringValue
    }

    private fun List<String>.revert() = woopMessage {
        id = ByteString.copyFromUtf8(Generator.randomString())
        createdAt = timestamp {
            val time: Instant = Instant.now()
            seconds = time.epochSecond
            nanos = time.nano
        }
        wrapper += messageWrapper {
            filters = filters {
                filter.addAll(this@revert)
            }
        }
    }

    private fun Entity.revert() = woopMessage {
        id = ByteString.copyFromUtf8(Generator.randomString())
        createdAt = timestamp {
            val time: Instant = Instant.now()
            seconds = time.epochSecond
            nanos = time.nano
        }
        wrapper += messageWrapper {
            entityCreate = entity {
                id = ByteString.copyFromUtf8(Generator.randomString())
                data = struct {
                    fields.put("data", value { stringValue = this@revert.data })
                }
                tags.addAll(this@revert.tags.map { tag ->
                    tag {
                        id = ByteString.copyFromUtf8(Generator.randomString())
                        data = struct {
                            fields.put("data", value { stringValue = tag })
                        }
                    }
                })
            }
        }
    }
}


data class Entity(
    val id: String,
    val data: String,
    val tags: List<String>,
)