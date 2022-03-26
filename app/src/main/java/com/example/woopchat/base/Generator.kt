package com.example.woopchat.base

import com.example.woopchat.base.Generator.nextInt
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

object Generator {

    private val nextInt = AtomicInteger()

    fun nextInt() = nextInt.getAndIncrement()

    fun randomString() = UUID.randomUUID().toString()

    fun randomColor() = (0xff shl 24) or
        (Random.nextInt(256) shl 16) or
            (Random.nextInt(256) shl 8) or
            Random.nextInt(256)
}