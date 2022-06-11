package com.example.woopchat.studying.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * FusibleFlow<T>.fuse() called only within buffer() and flowOn() operators
 */

private suspend fun docs() {
    var flow = channelFlow<Int> { }
    flow = MutableSharedFlow()
    flow = MutableStateFlow(3)

    flow.buffer()
    flow.flatMapMerge { flowOf<Int>() }
    flow.map { flowOf<Int>() }.flattenMerge()


    flow.produceIn(CoroutineScope(EmptyCoroutineContext))
    flow.shareIn(CoroutineScope(EmptyCoroutineContext), SharingStarted.Lazily)
    flow.stateIn(CoroutineScope(EmptyCoroutineContext), SharingStarted.Lazily, 3)
}

fun fusionFlowOn() {
    runBlocking {
        start(this)
        suspendCoroutine { cont: Continuation<Int> ->
            cont.resume(6)
        }
    }
}

//How will be fused
@OptIn(ExperimentalCoroutinesApi::class)
private suspend fun start(cs: CoroutineScope) {
    val sf = MutableStateFlow(0)
    val producer = createSingleThreadContext("producer")
    val consumer = createSingleThreadContext("consumer")

    val flow = channelFlow {
        repeat(1000) {
            delay(1)
            val value = computeSomeValue()
            println { "$value" }
            send(value)
        }
    }
        .buffer(UNLIMITED)
        .flowOn(producer)
        .stateIn(CoroutineScope(cs.newCoroutineContext(producer)))

    cs.launch(consumer) {
        flow.collect {
            println { "$it" }
        }
    }
}

private var value = 0
private fun computeSomeValue(): Int = value++

@OptIn(ObsoleteCoroutinesApi::class)
private fun createSingleThreadContext(name: String) = newSingleThreadContext(name)
//    Executors.newSingleThreadExecutor(DefaultThreadFactory(name)).asCoroutineDispatcher()
