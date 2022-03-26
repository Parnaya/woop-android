package com.example.woopchat

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
    lifecycle: Lifecycle,
) : BaseVimo() {
    private val provider = ChatProvider(
        lifecycle = lifecycle,
        scope = viewModelScope,
        serviceProvider = ServiceProvider(),
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
            provider.onReceiveMessage { Log.d("MESSAGE", "haha, message received: $") }
        }
        launchBg {
            val user = Generator.randomString()
            while (true) {
                delay(3000)
                provider.sendMessage(
                    Entity(
                        id = Generator.randomString(),
                        data = Generator.nextInt().toString(),
                        tags = listOf(
                            "user|$user",
                            "chat|$name",
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