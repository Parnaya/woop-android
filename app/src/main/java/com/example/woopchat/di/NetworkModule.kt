package com.example.woopchat.di

import android.app.Application
import android.content.res.AssetManager
import com.example.woopchat.base.AppConfig
import com.example.woopchat.base.BuildVariant
import com.example.woopchat.service.*
import com.example.woopchat.service.adapter.LocalDateTimeTypeAdapter
import com.example.woopchat.service.adapter.LocalDateTypeAdapter
import com.example.woopchat.service.adapter.OffsetDateTimeTypeAdapter
import com.example.woopchat.service.adapter.OffsetTimeTypeAdapter
import com.example.woopchat.service.deserialize.WoopMessageDeserializer
import com.example.woopchat.service.serialize.WoopMessageSerializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.MessageAdapter
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.StreamAdapter
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.retry.ExponentialWithJitterBackoffStrategy
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.pwall.json.schema.JSONSchema
import okhttp3.OkHttpClient
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.OffsetTime
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

//todo depends from app
//AssetManager
//AppConfig

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideJsonSchema(assets: AssetManager): JSONSchema = JSONSchema.parse(
        assets.open("schema/woop-socket-message.json").bufferedReader().readText()
    )

    @Provides
    @Singleton
    fun provideGson(schema: JSONSchema): Gson = GsonBuilder()
        .registerTypeAdapter(OffsetDateTime::class.java, OffsetDateTimeTypeAdapter().nullSafe())
        .registerTypeAdapter(OffsetTime::class.java, OffsetTimeTypeAdapter.Iso().nullSafe())
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter.Iso().nullSafe())
        .registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter.Iso().nullSafe())
        .registerTypeAdapter(WoopMessage::class.java, WoopMessageSerializer(schema))
        .registerTypeAdapter(WoopMessage::class.java, WoopMessageDeserializer(schema))
        .create()

    @Provides
    @Reusable
    fun provideWoopMessageAdapterFactory(config: AppConfig, gson: Gson): MessageAdapter.Factory = when (config.buildVariant) {
        BuildVariant.Debug -> LoggingMessageAdapter.Factory(
            GsonMessageAdapter.Factory(gson)
        )
        BuildVariant.Release -> GsonMessageAdapter.Factory(gson)
    }

    @Provides
    @Reusable
    fun provideStreamAdapterFactory(): StreamAdapter.Factory = CoroutinesStreamAdapterFactory()

    @Provides
    @Reusable
    fun provideLoggingInterceptor(): LoggingInterceptor = LoggingInterceptor()

    @Provides
    @Singleton
    fun provideOkhttpClient(config: AppConfig): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .apply { if (config.isDebug()) addInterceptor(LoggingInterceptor(isEnabled = true)) }
        .build()

    @Provides
    @Reusable
    fun provideLifecycle(application: Application): Lifecycle = AndroidLifecycle.ofApplicationForeground(application)

    @Provides
    @Singleton
    fun provideScarlet(okHttpClient: OkHttpClient, gson: Gson, lifecycle: Lifecycle): Scarlet { //TODO url not supposed to be here
//    private val host = "jusp.me"
//        private val host = "localhost:1323"
        val host = "10.10.13.154:1323"

        val url = "ws://${host}/ws"
//    private val url = "wss://demo.piesocket.com/v3/channel_7?api_key=oCdCMcMPQpbvNjUIzqtvF1d2X2okWpDQj4AwARJuAgtjhzKxVEjQU6IdCjwm&notify_self"

        val backoffStrategy = ExponentialWithJitterBackoffStrategy(5000, 5000)

        return Scarlet.Builder()
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
    }

    @Provides
    @Singleton
    fun provideWebsocketService(scarlet: Scarlet, config: AppConfig): WebsocketService {
        val service = scarlet.create<WebsocketService>()
        if (config.isDebug()) {
            GlobalScope.launch { //TODO create scope only for logging
                logConnectionState(this, service.observeWebSocketEvent())
            }
        }
        return service
    }
}