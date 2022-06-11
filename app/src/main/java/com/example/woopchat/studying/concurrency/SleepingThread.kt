package com.example.woopchat.studying.concurrency

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread
import kotlin.concurrent.withLock

fun logThread(string: String) {
    println("${System.currentTimeMillis() % 10000} : ${Thread.currentThread().name} $string")
}

private val lock = ReentrantLock()

val sleepingThread: Thread = thread(start = false, name = "sleeping") {
    lock.withLock {
        for (i in 0 until 10) {
            Thread.sleep(10)
            logThread("done work $i")
        }
    }
    Thread.yield()
    logThread("slept")
    try {
        Thread.sleep(1000)
    } catch (e: InterruptedException) {
        logThread("catch: ${Thread.currentThread().isInterrupted}")
    }
    logThread("finally: ${Thread.currentThread().isInterrupted}")
    logThread("awake")
    for (i in 10 until 20) {
        Thread.sleep(10)
        logThread("done work $i")
    }
}

fun exec() {
    sleepingThread.start()
    Thread.yield()
    logThread("resume main")
    lock.lock()
    sleepingThread.interrupt()
    logThread("interrupt: ${sleepingThread.isInterrupted}")
}