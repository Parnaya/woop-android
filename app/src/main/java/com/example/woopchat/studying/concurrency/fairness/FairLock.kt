package com.example.woopchat.studying.concurrency.fairness

import okhttp3.internal.notify
import okhttp3.internal.wait

class FairLock {
    private var isLocked = false
    private var lockingThread: Thread? = null
    private val waitingThreads: MutableList<QueueObject> = ArrayList()

    fun lock() {
        val queueObject = QueueObject()
        var isLockedForThisThread = true
        synchronized(this) { waitingThreads.add(queueObject) }
        while (isLockedForThisThread) {
            synchronized(this) {

                isLockedForThisThread = isLocked || waitingThreads[0] !== queueObject
                if (!isLockedForThisThread) {
                    isLocked = true
                    waitingThreads.remove(queueObject)
                    lockingThread = Thread.currentThread()
                    return
                }
            }
            try {
                queueObject.doWait()
            } catch (e: InterruptedException) {
                synchronized(this) { waitingThreads.remove(queueObject) }
                throw e
            }
        }
    }

    @Synchronized
    fun unlock() {
        if (lockingThread !== Thread.currentThread()) {
            throw IllegalMonitorStateException("Calling thread has not locked this lock")
        }
        isLocked = false
        lockingThread = null
        if (waitingThreads.size > 0) {
            waitingThreads[0].doNotify()
        }
    }
}

class QueueObject {
    private var isNotified = false

    @Synchronized
    fun doWait() {
        while (!isNotified) {
            wait()
        }
        isNotified = false
    }

    @Synchronized
    fun doNotify() {
        isNotified = true
        notify()
    }

    override fun equals(o: Any?): Boolean {
        return this === o
    }
}