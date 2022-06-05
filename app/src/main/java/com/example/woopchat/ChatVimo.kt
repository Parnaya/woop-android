package com.example.woopchat

import androidx.lifecycle.SavedStateHandle
import com.example.woopchat.base.BaseVimo
import com.example.woopchat.base.Generator
import com.example.woopchat.coroutines.launchBg
import com.example.woopchat.di.A
import com.example.woopchat.di.B
import com.example.woopchat.recycler.diff.Diffable
import com.example.woopchat.service.ChatUseCases
import com.example.woopchat.service.Entity
import com.example.woopchat.service.ISocketUseCases
import com.example.woopchat.utils.LiveState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class ChatVimo @Inject constructor(
    b: B,
    socketUseCases: ISocketUseCases,
    savedStateHandle: SavedStateHandle,
) : BaseVimo(),
    LiveState<ChatState> by LiveState() {

    private val userTag: String = ""
    private val chatTag: String = ""

    //Todo inject that dependencies
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
