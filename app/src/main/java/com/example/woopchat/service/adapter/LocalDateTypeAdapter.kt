package com.example.woopchat.service.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

abstract class LocalDateTypeAdapter(
    private val format: DateTimeFormatter
) : TypeAdapter<LocalDate>() {

    override fun write(out: JsonWriter, value: LocalDate) {
        out.value(format.format(value))
    }

    override fun read(`in`: JsonReader): LocalDate {
        val isoDateString = `in`.nextString()
        return format.parse(isoDateString, LocalDate.FROM)
    }

    class Iso : LocalDateTypeAdapter(format = DateTimeFormatter.ISO_LOCAL_DATE)
}
