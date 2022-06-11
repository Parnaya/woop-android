package com.example.woopchat.studying.coroutines

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*

private fun <T, R> Flow<T>.map(transform: (T) -> R): Flow<R> = flow {
    collect { emit(transform(it)) }
}

@OptIn(InternalCoroutinesApi::class)
private fun <T> Flow<T>.onComplete(action: (Throwable?) -> Unit): Flow<T> = flow {
    try {
        collect(this)
    } catch (e: Throwable) {

        //to upstream
        invokeSafely(action, e)
        throw e
    }

    //success action
    action(null)
}

private fun invokeSafely(action: (Throwable?) -> Unit, th: Throwable) {
    try {
        action.invoke(th)
    } catch (e: Throwable) {
        e.addSuppressed(th)
        throw e
    }
}

class ActionFlow<T>(private val action: (FlowCollector<T>) -> Unit) : Flow<T> {
    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<T>) {
        action(collector)
    }
}

class ProxyFlow<T>(private val flow: Flow<T>) : Flow<T> {
    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<T>) {
        flow.collect(collector)
    }
}

private suspend fun start() {
    flowOf(7)
        .onComplete { }
        .map {  }
        .collect { }

    flow<Int> { }
}


