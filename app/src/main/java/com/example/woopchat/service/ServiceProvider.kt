package com.example.woopchat.service

import com.example.woopchat.*
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter
import com.tinder.scarlet.messageadapter.protobuf.ProtobufMessageAdapter
import com.tinder.scarlet.retry.ExponentialWithJitterBackoffStrategy
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.util.concurrent.TimeUnit

class ServiceProvider {
        private val host = "jusp.me"
//    private val host = "localhost:1323"
//    private val host = "10.20.6.98:1323"
    private val url = "ws://${host}/ws"
    private val backoffStrategy = ExponentialWithJitterBackoffStrategy(5000, 5000)
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(LoggingInterceptor(isEnabled = true))
        .build()

    private val services: MutableMap<Lifecycle, WebsocketService> = mutableMapOf()

    fun getService(lifecycle: Lifecycle): WebsocketService {
        return services[lifecycle] ?: createService(lifecycle)
            .also { services[lifecycle] = it }
    }

    fun createService(lifecycle: Lifecycle): WebsocketService {
        lifecycle.also {
            it.subscribe(LifecycleLoggerSubscriber())
        }

        val scarlet = Scarlet.Builder()
            .webSocketFactory(okHttpClient.newWebSocketFactory(url))
            .addMessageAdapterFactory(LoggingMessageAdapter.Factory(GsonMessageAdapter.Factory()))
            .addStreamAdapterFactory(CoroutinesStreamAdapterFactory())
            .backoffStrategy(backoffStrategy)
            .lifecycle(lifecycle)
            .build()

        val service = scarlet.create<WebsocketService>()
        GlobalScope.launch { //TODO create scope only for logging
            logConnectionState(this, service.observeWebSocketEvent())
        }
        return service
    }
}