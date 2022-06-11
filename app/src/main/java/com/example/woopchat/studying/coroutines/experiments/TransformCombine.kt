package com.example.woopchat.studying.coroutines.experiments

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * FusibleFlow<T>.fuse() called only within buffer() and flowOn() operators
 */

private suspend fun docs() {
    var flow = flow<Int> { }
    flow.combine(flow = flowOf(1), transform = { a, b -> a + b})
    flow.combineTransform(flowOf(1), transform = { a, b -> emit(a + b) })
}

fun runTransform() {
    runBlocking {
        start(this)
    }
}

//How will be fused
@OptIn(ExperimentalCoroutinesApi::class)
private suspend fun start(cs: CoroutineScope) {
    val flow1 = flow<Int> {
        repeat(10) {
            emit(it)
        }
    }

    val flow2 = flow<String> {
        repeat(10) {
            emit(('a' + it).toString())
        }
    }

    val flow = combineTransform(flow1, flow2, transform = { a, b ->
        emit("1 - $a$b")
        println("after 1 - $a$b")
        emit("2 - $a$b")
        println("after 2 - $a$b")
        emit("3 - $a$b")
        println("after 3 - $a$b")
    })
        .map { "total: $it" }



    cs.launch {
        flow.collect {
            println(it)
        }
    }
}
