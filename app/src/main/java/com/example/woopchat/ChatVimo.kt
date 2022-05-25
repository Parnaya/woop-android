package com.example.woopchat

import androidx.lifecycle.ViewModel
import com.example.woopchat.base.BaseVimo
import com.example.woopchat.base.Generator
import com.example.woopchat.coroutines.launchBg
import com.example.woopchat.di.ChatScope
import com.example.woopchat.di.VimoFactory
import com.example.woopchat.recycler.diff.Diffable
import com.example.woopchat.service.ChatUseCases
import com.example.woopchat.service.Entity
import com.example.woopchat.service.SocketUseCases
import com.example.woopchat.utils.LiveState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import org.threeten.bp.OffsetDateTime

class ChatVimo @AssistedInject constructor(
    @Assisted("userTag") private val userTag: String,
    @Assisted("chatTag") private val chatTag: String,
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

    fun onScrollToEnd() = launchBg {
        val res = useCases.ensurePagination()
        if (res) {
            useCases.loadMessages()
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("userTag") userTag: String,
            @Assisted("chatTag") chatTag: String,
        ): ChatVimo
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
