import android.os.Build
import androidx.annotation.RequiresApi
import com.example.woopchat.studying.concurrency.SomeWork
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import java.util.concurrent.locks.StampedLock
import kotlin.concurrent.thread

private val latch = CountDownLatch(50)

private val log = StringBuilder()

@RequiresApi(Build.VERSION_CODES.N)
private val lock = ReentrantReadWriteLock()

private fun logSb(string: String) {
    log.append("${System.currentTimeMillis() % 10000} : ${Thread.currentThread().name} ====== $string")
    log.append("\n")
}

@RequiresApi(Build.VERSION_CODES.N)
private val work = object : SomeWork {
    private val buffer = ArrayDeque<Runnable>()
    private val buffSize = 10
    private val writeLock = lock.writeLock()
    private val readLock = lock.readLock()
    private val bufferNotFull = writeLock.newCondition()
    private val bufferNotEmpty = readLock.newCondition()

    override fun produce(workload: Runnable) {
        writeLock.lock()
        while (buffer.size == buffSize) {
            bufferNotFull.await()
        }
        buffer.add(workload)
        if (buffer.isNotEmpty()) {
            bufferNotEmpty.signal()
        }
        logSb("produce, buffer size = ${buffer.size}")
        writeLock.unlock()
    }

    override fun consume(): Runnable {
        readLock.lock()
        while (buffer.isEmpty()) {
            bufferNotEmpty.await()
        }
        return buffer.pop().also {
            if (buffer.size == buffSize - 1) {
                logSb("invoke notify")
                bufferNotFull.signal()
            }
            latch.countDown()
            logSb("consume, buffer size = ${buffer.size}, left: ${latch.count}")
            readLock.unlock()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
private fun dump() {
    producers.forEach {
        logSb("${it.name} ${it.state.name}")
    }
    consumers.forEach {
        logSb("${it.name} ${it.state.name}")
    }
}

@RequiresApi(Build.VERSION_CODES.N)
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

@RequiresApi(Build.VERSION_CODES.N)
private val consumers = List(5) {
    thread(name = "Consumer thread $it", start = false) {
        while (latch.count > 0) {
            work.consume().run()
        }
    }
}


@RequiresApi(Build.VERSION_CODES.N)
fun stampedLock() {
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