package com.example.woopchat.studying.coroutines.experiments

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Mock {


    suspend fun decompilation() {
        val hh = "some local field"
        println("action 1")
        delay(100)
        println("after delay")
        someFoo()
        println("end - $hh")
    }

    suspend fun someFoo() {
        GlobalScope.launch {
            delay(100)
            println("100")
        }
        GlobalScope.async {

        }
    }
}