package com.example.woopchat.service.deserialize

import android.util.Log
import com.example.woopchat.service.Entity
import com.example.woopchat.service.WoopMessage
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.pwall.json.schema.JSONSchema
import java.lang.reflect.Type

class WoopMessageDeserializer(
    private val scheme: JSONSchema,
) : JsonDeserializer<WoopMessage> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): WoopMessage {
        val root = json as? JsonObject ?: throw IllegalArgumentException("Cannot deserialize Woop Message")

        val res = scheme.validateBasic(root.toString())
        if (!res.valid) Log.e(
            "===JsonSchema===",
            res.errors?.joinToString(" ==== ") { "${it.error} - ${it.instanceLocation}" }  ?: "null"
        )

        val entities = root.jsonArray("messages") {
            context.deserialize<Entity>(this, Entity::class.java)
            obj("data") {
                context.deserialize<Entity>(this, Entity::class.java)
            }
        }

        return WoopMessage.Entities(entities)
    }

    private fun JsonObject.str(name: String): String {
        return get(name)?.asString ?: error("String `$name` not found")
    }

    private inline fun <T> JsonObject.jsonArray(name: String, converter: JsonObject.(Int) -> T): List<T> {
        return get(name)?.asJsonArray?.mapIndexed { index, jsonElement ->
            jsonElement.asJsonObject.converter(index)
        } ?: error("JSON array `$name` not found")
    }

    private fun JsonObject.strList(name: String): List<String> {
        return get(name)?.asJsonArray?.map { it.asString } ?: error("String array `$name` not found")
    }

    //region JsonObject extensions
    private inline fun <T> JsonObject.obj(name: String, converter: JsonObject.() -> T ): T {
        return get(name)?.asJsonObject?.let(converter) ?: error("JSON object `$name` not found")
    }

    private inline fun <T> JsonObject.objOrNull(name: String, converter: JsonObject.() -> T): T? {
        return get(name)?.asJsonObject?.let(converter)
    }
}