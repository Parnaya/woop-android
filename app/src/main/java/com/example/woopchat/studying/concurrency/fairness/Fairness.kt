package com.example.woopchat.studying.concurrency.fairness

import okhttp3.internal.wait
import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread

private val latch = CountDownLatch(20)
private object Work {
    val fairLock = FairLock()
    fun doWork() {
        var long: Long = 0L
        fairLock.lock()
        for (i in Long.MIN_VALUE until Long.MAX_VALUE) {
//            for (j in Long.MIN_VALUE until Long.MAX_VALUE) {
                long = i
//            }
        }
        latch.countDown()
        fairLock.unlock()
    }
}

fun fairness() {
    for (i in 0 until 20) {
        thread {
            Work.doWork()
        }
    }
    latch.await()
}