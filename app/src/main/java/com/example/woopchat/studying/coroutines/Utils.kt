package com.example.woopchat.studying.coroutines

import kotlinx.coroutines.AbstractCoroutine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.job
import java.lang.Thread.currentThread
import kotlin.reflect.full.memberFunctions

inline fun CoroutineScope.println(tag: String = "===========", block: () -> String) {
    kotlin.io.println("${currentThread()}: ${block()}")
}

@OptIn(InternalCoroutinesApi::class)
fun CoroutineScope.getName(): String {
    val nameString = AbstractCoroutine::class.memberFunctions.single { it.name == "nameString" } as Function1<AbstractCoroutine<*>, String>
    val name = nameString(coroutineContext.job as AbstractCoroutine<*>)
    return name
}