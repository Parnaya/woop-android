package com.example.woopchat

import android.app.Application
import android.content.res.AssetManager
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.woopchat.base.BaseVimo
import com.example.woopchat.base.Generator
import com.example.woopchat.coroutines.launchBg
import com.example.woopchat.service.ChatProvider
import com.example.woopchat.service.Entity
import com.example.woopchat.service.ServiceProvider
import com.tinder.scarlet.Lifecycle
import kotlinx.coroutines.delay

class MainVimo(
    assets: AssetManager,
    lifecycle: Lifecycle,
) : BaseVimo() {
    private val provider = ChatProvider(
        lifecycle = lifecycle,
        scope = viewModelScope,
        serviceProvider = ServiceProvider(assets),
    )

    fun onOpenedChat(name: String) {
        //todo clear chat
        launchBg {
            val filters = listOf(
                "chat|$name",
            )
            provider.updateFilters(filters)
        }
        launchBg {
            provider.onReceiveMessage { Log.d("MESSAGE", "haha, message received: $it") }
        }
        launchBg {
            val user = Generator.randomString()
            while (true) {
                delay(10000)
                provider.sendMessage(
                    Entity(
                        id = Generator.randomString(),
                        data = Entity.Data("some text"),
                        tags = listOf(
                            "user|$user",
                            "chat|/second",
                            "type|message",
                        ),
                    )
                )
            }
        }
    }

    companion object {
        val Screens = listOf(
            Screen("e63d8997-88b8-4261-a19a-ca5ff887f14a", "Тет а тет"),
            Screen("/", "Main"),
            Screen("/second", "Second"),
        )
    }
}

data class Screen(
    val id: String,
    val title: String,
)