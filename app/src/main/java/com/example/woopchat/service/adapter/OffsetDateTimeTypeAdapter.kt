package com.example.woopchat.service.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

class OffsetDateTimeTypeAdapter: TypeAdapter<OffsetDateTime>() {
    val writeFormat = DateTimeFormatter.ISO_INSTANT
    val readFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    override fun write(out: JsonWriter, value: OffsetDateTime) {
        out.value(writeFormat.format(value))
    }

    override fun read(`in`: JsonReader): OffsetDateTime {
        val isoDateString = `in`.nextString()
        return readFormat.parse(isoDateString, OffsetDateTime.FROM)
    }
}
