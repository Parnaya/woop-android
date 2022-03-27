package com.example.woopchat.service.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.threeten.bp.OffsetTime
import org.threeten.bp.format.DateTimeFormatter

abstract class OffsetTimeTypeAdapter(
    private val format: DateTimeFormatter
) : TypeAdapter<OffsetTime>() {

    override fun write(out: JsonWriter, value: OffsetTime) {
        out.value(format.format(value))
    }

    override fun read(`in`: JsonReader): OffsetTime {
        val isoDateString = `in`.nextString()
        return format.parse(isoDateString, OffsetTime.FROM)
    }

    class Iso : OffsetTimeTypeAdapter(format = DateTimeFormatter.ISO_TIME)
}
