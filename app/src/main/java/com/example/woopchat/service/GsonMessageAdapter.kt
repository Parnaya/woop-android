package com.example.woopchat.service

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.tinder.scarlet.Message
import com.tinder.scarlet.MessageAdapter
import java.io.OutputStreamWriter
import java.io.StringReader
import okio.Buffer
import java.nio.charset.StandardCharsets.UTF_8
import java.lang.reflect.Type



interface Interceptor {
    fun interceptFrom() {

    }

    fun interceptTo() {

    }
}

class GsonMessageAdapter<T> private constructor(
    private val gson: Gson,
    private val typeAdapter: TypeAdapter<T>
) : MessageAdapter<T> {

    override fun fromMessage(message: Message): T {
        val stringValue = when (message) {
            is Message.Text -> message.value
            is Message.Bytes -> String(message.value)
        }
        val jsonReader = gson.newJsonReader(StringReader(stringValue))
        return typeAdapter.read(jsonReader)!!
    }

    override fun toMessage(data: T): Message {
        val buffer = Buffer()
        val writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
        val jsonWriter = gson.newJsonWriter(writer)
        typeAdapter.write(jsonWriter, data)
        jsonWriter.close()
        val stringValue = buffer.readByteString().utf8()
        return Message.Text(stringValue)
    }

    class Factory(
        private val gson: Gson = DEFAULT_GSON,
//        schemes: Map<Type, String>,
//        vararg interceptors: Interceptor,
    ) : MessageAdapter.Factory {

        override fun create(type: Type, annotations: Array<Annotation>): MessageAdapter<*> {
            val typeAdapter = gson.getAdapter(TypeToken.get(type))
            return GsonMessageAdapter(gson, typeAdapter)
        }

        companion object {
            private val DEFAULT_GSON = Gson()
        }
    }
}