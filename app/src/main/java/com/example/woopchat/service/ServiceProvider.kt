package com.example.woopchat.service

import android.content.res.AssetManager
import com.example.woopchat.LifecycleLoggerSubscriber
import com.example.woopchat.LoggingInterceptor
import com.example.woopchat.LoggingMessageAdapter
import com.example.woopchat.logConnectionState
import com.example.woopchat.service.adapter.LocalDateTimeTypeAdapter
import com.example.woopchat.service.adapter.LocalDateTypeAdapter
import com.example.woopchat.service.adapter.OffsetDateTimeTypeAdapter
import com.example.woopchat.service.adapter.OffsetTimeTypeAdapter
import com.example.woopchat.service.deserialize.WoopMessageDeserializer
import com.example.woopchat.service.serialize.WoopMessageSerializer
import com.google.gson.GsonBuilder
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.retry.ExponentialWithJitterBackoffStrategy
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.pwall.json.schema.JSONSchema
import okhttp3.OkHttpClient
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.OffsetTime
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.URL
import java.util.concurrent.TimeUnit


class ServiceProvider(
    private val assets: AssetManager,
) {
    private val host = "jusp.me"
//        private val host = "localhost:1323"
//    private val host = "10.10.13.154:1323"

    private val url = "ws://${host}/ws"
//    private val url = "wss://demo.piesocket.com/v3/channel_1?api_key=oCdCMcMPQpbvNjUIzqtvF1d2X2okWpDQj4AwARJuAgtjhzKxVEjQU6IdCjwm&notify_self"
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

        val text = assets.open("schema/woop-socket-message.json").bufferedReader().readText()
        val schema = JSONSchema.parse(text)

        val gson = GsonBuilder()
            .registerTypeAdapter(OffsetDateTime::class.java, OffsetDateTimeTypeAdapter.Iso().nullSafe())
            .registerTypeAdapter(OffsetTime::class.java, OffsetTimeTypeAdapter.Iso().nullSafe())
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter.Iso().nullSafe())
            .registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter.Iso().nullSafe())
            .registerTypeAdapter(WoopMessage::class.java, WoopMessageSerializer(schema))
            .registerTypeAdapter(WoopMessage::class.java, WoopMessageDeserializer(schema))
            .create()

        val scarlet = Scarlet.Builder()
            .webSocketFactory(okHttpClient.newWebSocketFactory(url))
            .addMessageAdapterFactory(
                LoggingMessageAdapter.Factory(
                    GsonMessageAdapter.Factory(gson)
                )
            )
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

sealed class WoopMessage {
    data class Filter(
        val filter: String,
    ) : WoopMessage()

    data class Entities(
        val entities: List<Entity>
    ) : WoopMessage()

    companion object {
        const val Insert = "insert"
        const val Filter = "filters"
    }
}

data class Entity(
    val id: String,
    val createdAt: OffsetDateTime,
    val data: Data,
    val tags: List<String>,
) {
    data class Data(
        val text: String,
    )
}