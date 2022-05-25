package com.example.woopchat.coroutines

import kotlinx.coroutines.CoroutineExceptionHandler

interface ExceptionHandlers {
    val io: CoroutineExceptionHandler

    companion object : ExceptionHandlers {
        override var io = CoroutineExceptionHandler { _, throwable -> println(throwable) }
    }
}