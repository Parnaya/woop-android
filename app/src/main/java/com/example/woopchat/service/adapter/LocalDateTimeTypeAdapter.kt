package com.example.woopchat.service.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

abstract class LocalDateTimeTypeAdapter(
    private val format: DateTimeFormatter
) : TypeAdapter<LocalDateTime>() {

    override fun write(out: JsonWriter, value: LocalDateTime) {
        out.value(format.format(value))
    }

    override fun read(`in`: JsonReader): LocalDateTime {
        val isoDateString = `in`.nextString()
        return format.parse(isoDateString, LocalDateTime.FROM)
    }

    class Iso : LocalDateTimeTypeAdapter(format = DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}
