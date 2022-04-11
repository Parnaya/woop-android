package com.example.woopchat

import com.example.woopchat.base.BaseVimo
import com.example.woopchat.base.Generator
import com.example.woopchat.coroutines.launchBg
import com.example.woopchat.live_data.LiveActions
import com.example.woopchat.recycler.diff.Diffable
import com.example.woopchat.service.ChatUseCases
import com.example.woopchat.service.Entity
import com.example.woopchat.service.SocketUseCases
import com.example.woopchat.utils.LiveState
import kotlinx.coroutines.launch
import org.threeten.bp.OffsetDateTime

class ChatVimo(
    private val userTag: String,
    private val chatTag: String,
    socketUseCases: SocketUseCases,
) : BaseVimo(),
    LiveState<ChatState> by LiveState() {

    private val useCases = ChatUseCases(userTag, chatTag, socketUseCases)
    private val converter = ChatVistConverter(userTag)

    init { //request filter
        coroutineScope.launch {
            useCases.onUpdates {
                setState(converter.convert(it))
            }
        }
    }

    fun sendMessage(text: String): List<Diffable> {
        val entity = Entity(
            id = Generator.randomString(),
            createdAt = OffsetDateTime.now(),
            data = Entity.Data(text),
            tags = listOf(userTag, chatTag, ChatUseCases.MessageType)
        )
        launchBg {
            useCases.sendMessage(entity)
        }
        return converter.convertEntity(entity).let(::listOf)
    }

    fun onScrollToEnd() {
        val res = useCases.ensurePagination()
        if (!res) return
        launchBg {
            useCases.loadMessages()
        }
    }
}

data class ChatState(
    val items: List<Diffable>,
)

class ChatVistConverter(
    private val userTag: String,
) {
    fun convert(data: List<Entity>): ChatState {
        val items = data.map(::convertEntity)
        return ChatState(items)
    }

    fun convertEntity(it: Entity): Diffable {
        return if (it.tags.contains(userTag)) RightMessage(
            text = it.data.text,
            id = it.id,
        ) else LeftMessage(
            text = it.data.text,
            id = it.id,
        )
    }
}
