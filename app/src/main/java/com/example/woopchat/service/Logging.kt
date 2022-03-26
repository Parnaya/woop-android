package com.example.woopchat

import android.util.Log
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.Message
import com.tinder.scarlet.MessageAdapter
import com.tinder.scarlet.WebSocket
import io.reactivex.subscribers.DisposableSubscriber
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicInteger

class LoggingInterceptor(
    isEnabled: Boolean
) : Interceptor {

    private val interceptor = HttpLoggingInterceptor(
        object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                logInfo<LoggingInterceptor>(message)
            }
        }
    ).setLevel(if (isEnabled) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)

    override fun intercept(chain: Interceptor.Chain): Response {
        return interceptor.intercept(chain)
    }
}

inline fun <reified T> logInfo(msg: String, error: Throwable? = null) {
    Log.i(simpleNameOf<T>(), msg, error)
}

inline fun <reified T> simpleNameOf(): String {
    return T::class.java.simpleName
}


class LoggingMessageAdapter<T>(
    private val messageAdapter: MessageAdapter<T>,
) : MessageAdapter<T> {
    override fun fromMessage(message: Message): T {
        Log.d("========", "RECEIVE <-- ${message.asString()}")
        return messageAdapter.fromMessage(message)
    }

    override fun toMessage(data: T): Message =messageAdapter
        .toMessage(data)
        .also {
            Log.d("=======", "SEND --> ${it.asString()}")
        }

    private fun Message.asString(): String =
        when (this) {
            is Message.Text ->
                value
            is Message.Bytes ->
                value.toString()
        }


    class Factory(
        private val factory: MessageAdapter.Factory
    ) : MessageAdapter.Factory {
        override fun create(type: Type, annotations: Array<Annotation>): MessageAdapter<*> =
            LoggingMessageAdapter(factory.create(type, annotations))
    }
}

class LifecycleLoggerSubscriber(
) : DisposableSubscriber<Lifecycle.State>() {
    private val pendingRequestCount = AtomicInteger()

    override fun onStart() {
        Log.d("=======Lifecycle", "Start")
        return request(1)
    }

    override fun onNext(lifecycleState: Lifecycle.State) {
        Log.d("=======Lifecycle", "Next --> ${lifecycleState.javaClass.simpleName}")
        val value = pendingRequestCount.decrementAndGet()
        if (value < 0) {
            pendingRequestCount.set(0)
        }
    }

    override fun onComplete() {
        Log.d("=======Lifecycle", "Complete")
    }

    override fun onError(throwable: Throwable) {
        Log.d("=======Lifecycle", "Error --> ${throwable.message}")
    }
}

fun logConnectionState(
    scope: CoroutineScope,
    channel: ReceiveChannel<WebSocket.Event>,
) {
    scope.launch(Dispatchers.IO) {
        while (true) {
            val state = channel.receive()
            when (state) {
                is WebSocket.Event.OnConnectionOpened<*> -> {
                    Log.d("=======Connection", "Opened --> ${state.webSocket}")
                }
                is WebSocket.Event.OnConnectionClosed -> {
                    Log.d("=======Connection", "OnConnectionClosed --> ${state.shutdownReason.code}: ${state.shutdownReason.reason}")
                }
                is WebSocket.Event.OnConnectionClosing -> {
                    Log.d("=======Connection", "OnConnectionClosing --> ${state.shutdownReason.code}: ${state.shutdownReason.reason}")
                }
                is WebSocket.Event.OnConnectionFailed -> {
                    Log.d("=======Connection", "OnConnectionFailed --> ${state.throwable}")
                }
                is WebSocket.Event.OnMessageReceived -> {
                    Log.d("=======Connection", "OnMessageReceived  --> ${state.message}")
                }
            }
        }
    }

}