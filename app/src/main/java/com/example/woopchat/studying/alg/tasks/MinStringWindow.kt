package com.example.woopchat.studying.alg.tasks

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

class MinStringWindow {
    val cOrder = LinkedList<Pair<Char, Int>>()

    @RequiresApi(Build.VERSION_CODES.N)
    fun minWindow(s: String, t: String): String {
        var range = 0..Int.MAX_VALUE

        val ocurT = HashMap<Char, Int>() //t ocurrences
        t.forEach { ch ->
            ocurT[ch] = (ocurT[ch] ?: 0) + 1
        }

        s.forEachIndexed { ind, ch ->
            if (ocurT.contains(ch)) {
                if (ocurT[ch] == 0) {
                    cOrder.removeFirst { it.first == ch }
                } else {
                    ocurT[ch] = (ocurT[ch] ?: 1) - 1
                }
                cOrder.add(ch to ind)
                if (cOrder.size == t.length && cOrder.last().second - cOrder.first().second < range.last - range.first) {
                    range = cOrder.first().second..cOrder.last().second
                }
            }
        }
        return if (cOrder.size != t.length) "" else s.substring(range)
    }

    fun <T> LinkedList<T>.removeFirst(predicate: (T) -> Boolean) {
        val iter = iterator()
        while (iter.hasNext()) {
            val el = iter.next()
            if (predicate(el)) {
                iter.remove()
                return
            }
        }
    }
}