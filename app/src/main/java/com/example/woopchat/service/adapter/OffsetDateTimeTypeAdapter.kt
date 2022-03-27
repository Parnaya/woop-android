package com.example.woopchat.service.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

abstract class OffsetDateTimeTypeAdapter(
    private val format: DateTimeFormatter
) : TypeAdapter<OffsetDateTime>() {

    override fun write(out: JsonWriter, value: OffsetDateTime) {
        out.value(format.format(value))
    }

    override fun read(`in`: JsonReader): OffsetDateTime {
        val isoDateString = `in`.nextString()
        return format.parse(isoDateString, OffsetDateTime.FROM)
    }

    class Iso : OffsetDateTimeTypeAdapter(format = DateTimeFormatter.ISO_OFFSET_DATE_TIME)
}
