package com.example.woopchat.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

fun CoroutineScope.launchMainNow(
    context: CoroutineContext = Dispatchers.mainImmediate,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) = launch(context, start, block)

fun CoroutineScope.launchMain(
    context: CoroutineContext = Dispatchers.main,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) = launch(context, start, block)

fun CoroutineScope.launchBg(
    context: CoroutineContext = Dispatchers.bg,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) = launch(context, start, block)

fun CoroutineScope.launchIo(
    context: CoroutineContext = Dispatchers.io,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) = launch(context, start, block)