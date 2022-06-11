package com.example.woopchat.studying.concurrency

import java.util.*
import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread

private val latch = CountDownLatch(50)

private val log = StringBuilder()
private val lock = Object()

private fun logSb(string: String) {
    log.append("${System.currentTimeMillis() % 10000} : ${Thread.currentThread().name} ====== $string")
    log.append("\n")
}

private val work = object : SomeWork {
    private val buffer = ArrayDeque<Runnable>()
    private val buffSize = 10

    override fun produce(workload: Runnable) {
        synchronized(lock) {
            synchronized(lock) {
                while (buffer.size == buffSize) {
                    lock.wait()
                }
                if (buffer.isEmpty()) lock.notifyAll()
                buffer.add(workload)
                logSb("produce, buffer size = ${buffer.size}")
            }
        }
    }

    override fun consume(): Runnable {
        synchronized(lock) {
            synchronized(lock) {
                while (buffer.isEmpty()) {
                    lock.wait()
                }
                if (buffer.size == buffSize) {
                    logSb("invoke notify")
                    lock.notifyAll()
                }
                return buffer.pop().also {
                    latch.countDown()
                    logSb("consume, buffer size = ${buffer.size}, left: ${latch.count}")
                }
            }
        }
    }
}

private fun dump() {
    producers.forEach {
        logSb("${it.name} ${it.state.name}")
    }
    consumers.forEach {
        logSb("${it.name} ${it.state.name}")
    }
}

private val producers = List(5) {
    thread(name = "Producer thread $it", start = false) {
        for (i in 0 until 10) {
            Thread.sleep(2)
            work.produce {
//                logSb("work started")
                Thread.sleep(10)
//                logSb("work ended")
            }
        }
    }
}

private val consumers = List(5) {
    thread(name = "Consumer thread $it", start = false) {
        while (latch.count > 0) {
            work.consume().run()
        }
    }
}


fun waitNotify() {
    producers.forEach {
        it.start()
    }
    consumers.forEach {
        it.start()
    }
    thread {
        Thread.sleep(5000)
        synchronized(lock) {
//            producers.forEach {
//                println("${it.name} ${it.state.name}")
//            }
//            consumers.forEach {
//                println("${it.name} ${it.state.name}")
//            }
            for (it in 0 until latch.count) {
                latch.countDown()
            }
        }
    }
    latch.await()
    println(log.toString())
}