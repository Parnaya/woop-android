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


private val lockTakenBy = mutableMapOf<Any, Thread>()
private val lockRequestedBy = mutableMapOf<Any, Set<Thread>>()

class TreeNode {
    private var parent: TreeNode? = null
    private val children = ArrayList<TreeNode>()

    @Synchronized
    fun addChild(child: TreeNode) {
        lockRequestedBy.remove(this)
        lockTakenBy[this] = Thread.currentThread()
        if (!children.contains(child)) {
            children.add(child)
            lockRequestedBy[child] = setOf(Thread.currentThread()) + (lockRequestedBy[child] ?: setOf())
            child.setParentOnly(this)
        }
        lockTakenBy.remove(this)
    }

    @Synchronized
    private fun addChildOnly(child: TreeNode) {
        if (!children.contains(child)) {
            children.add(child)
        }
    }

    @Synchronized
    fun setParent(parent: TreeNode) {
        this.parent = parent
        parent.addChildOnly(this)
    }


    @Synchronized
    fun setParentOnly(parent: TreeNode) {
        this.parent = parent
    }
}

class Locker {
    @Synchronized
    fun lock(other: Locker) {
        other.lock(this)
    }

    @Synchronized
    fun print(other: Locker) {
        println("$this print")
    }
}

fun deadLock() {
    val first = TreeNode()
    val second = TreeNode()
    thread { first.addChild(second) }
    thread { second.setParent(first) }
}