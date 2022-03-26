package com.example.woopchat.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers as KotlinDispatchers

interface Dispatchers {
    /** Dispatcher for UI operations */
    val main: CoroutineDispatcher
    /** Dispatcher for UI operations that may run without dispatching */
    val mainImmediate: CoroutineDispatcher
    /** Dispatcher for background operations */
    val bg: CoroutineDispatcher
    /** Dispatcher for long running IO operations */
    val io: CoroutineDispatcher

    companion object : Dispatchers {

        override var main: CoroutineDispatcher = KotlinDispatchers.Main
        override var mainImmediate: CoroutineDispatcher = KotlinDispatchers.Main.immediate
        override var bg: CoroutineDispatcher = KotlinDispatchers.Default
        override var io: CoroutineDispatcher = KotlinDispatchers.IO

        fun resetMain() {
            main = KotlinDispatchers.Main
        }

        fun resetBg() {
            bg = KotlinDispatchers.Default
        }

        fun resetIo() {
            io = KotlinDispatchers.IO
        }

        fun reset() {
            resetMain()
            resetBg()
            resetIo()
        }
    }
}
