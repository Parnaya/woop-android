package com.example.woopchat.service.serialize

import android.util.Log
import com.example.woopchat.base.Generator
import com.example.woopchat.service.Entity
import com.example.woopchat.service.WoopMessage
import com.google.gson.*
import net.pwall.json.schema.JSONSchema
import org.threeten.bp.Instant
import java.lang.reflect.Type

class WoopMessageSerializer(
    private val scheme: JSONSchema
) : JsonSerializer<WoopMessage> {

    override fun serialize(src: WoopMessage, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        src ?: return JsonNull.INSTANCE

        val jsonObject = jsonObject {
            add("id", Generator.randomString())
            add("createdAt", Instant.now().toString())
            addArray("messages") {
                when (src) {
                    is WoopMessage.Entities -> src.entities.forEach { entity ->
                        addObject {
                            add("type", WoopMessage.Insert)
                            add("data", context.serialize(entity))
                        }
                    }
                    is WoopMessage.Filter -> addObject {
                        add("type", WoopMessage.Filter)
                        add("data", src.filter)
                    }
                }
            }
        }

        val res = scheme.validateBasic(jsonObject.toString())
        if (!res.valid) Log.e(
            "===JsonSchema===",
            res.errors?.joinToString(" ==== ") { "${it.error} - ${it.instanceLocation}" } ?: "null"
        )
        return jsonObject
    }

    inline fun jsonObject(builder: JsonObject.() -> Unit) = JsonObject().apply(builder)
    inline fun jsonArray(builder: JsonArray.() -> Unit) = JsonArray().apply(builder)

    fun JsonObject.add(name: String, value: String) {
        addProperty(name, value)
    }

    inline fun JsonObject.addObject(name: String, builder: JsonObject.() -> Unit) {
        add(name, JsonObject().apply(builder))
    }

    inline fun JsonObject.addArray(name: String, builder: JsonArray.() -> Unit) {
        add(name, JsonArray().apply(builder))
    }

    inline fun JsonArray.addObject(builder: JsonObject.() -> Unit) {
        add(JsonObject().apply(builder))
    }

    inline fun JsonArray.addArray(name: String, builder: JsonArray.() -> Unit) {
        addAll(JsonArray().apply(builder))
    }
}