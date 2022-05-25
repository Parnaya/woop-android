package com.example.woopchat

import android.content.res.AssetManager
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.woopchat.base.BaseVimo
import com.example.woopchat.base.Generator
import com.example.woopchat.coroutines.launchBg
import com.example.woopchat.service.Entity
import com.example.woopchat.service.SocketUseCases
import com.example.woopchat.service.WebsocketServiceProvider
import com.tinder.scarlet.Lifecycle
import kotlinx.coroutines.delay
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

class MainVimo @Inject constructor(
    private val useCases: SocketUseCases,
) : BaseVimo() {

    fun onOpenedChat(position: Int) {
        //todo clear chat
        launchBg {
            val filter = Screens[position].id
            useCases.updateFilters(filter.let(::listOf))
        }
//        launchBg {
//            val user = Generator.randomString()
//            while (true) {
//                delay(10000)
//                useCases.sendMessage(
//                    Entity(
//                        id = Generator.randomString(),
//                        createdAt = OffsetDateTime.now(),
//                        data = Entity.Data("some text"),
//                        tags = listOf(
//                            "user|$user",
//                            "chat|/second",
//                            "type|message",
//                        ),
//                    )
//                )
//            }
//        }
    }

    companion object {
        val Screens = listOf(
            Screen("chat|/e63d8997-88b8-4261-a19a-ca5ff887f14a", "Тет а тет"), //TODO save id in SP
            Screen("chat|/", "Main"),
            Screen("chat|/second", "Second"),
        )
        val UserTag = "user|${Generator.randomString()}"
    }
}

data class Screen(
    val id: String,
    val title: String,
)