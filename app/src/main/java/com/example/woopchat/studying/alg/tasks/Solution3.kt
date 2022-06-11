package com.example.woopchat.studying.alg.tasks

class Solution3 {
    lateinit var startTime: IntArray
    lateinit var endTime: IntArray
    lateinit var profit: IntArray

    lateinit var cache: IntArray

    fun jobScheduling(startTime: IntArray, endTime: IntArray, profit: IntArray): Int {
        this.startTime = startTime //if start time sorted by ascending
        this.endTime = endTime
        this.profit = profit
        cache = IntArray(profit.size) { -1 }

        sort(0, profit.lastIndex)

        return schedule2()
    }

    fun schedule2(): Int { //without rec
        cache[0] = profit[0]

        for (i in 1..profit.lastIndex) {
            val last = findPrev(i)
            val prev = if (last == -1) 0 else cache[last]
            cache[i] = maxOf(prev + profit[i], cache[i-1])
        }
        return cache[profit.lastIndex]
    }


    fun schedule(from: Int): Int {//from, last - indexes
        if (from == profit.size) return 0
        if (cache[from] != -1) return cache[from]

        val next = findNext(endTime[from], from + 1, profit.lastIndex)
        val pick = profit[from] + if (next == -1) 0 else schedule(next)
        val notpick = schedule(from + 1)
        val max = maxOf(pick, notpick)
        cache[from] = max
        return max
    }

    fun findPrev(to: Int): Int {
        val startTime = startTime[to]
        for (i in to - 1 downTo 0) {
            if (endTime[i] <= startTime) return i
        }
        return -1
    }

    fun findNext(endTime: Int, from: Int, to: Int = profit.lastIndex): Int {
        for (i in from..to) {
            if (startTime[i] >= endTime) return i
        }
        return -1
    }

    fun sort(from: Int, to: Int) { //dual pivot quick sort
        val (p1, p2) = partition(from, to)
        if (p1 - from >= 2) sort(from, p1 - 1)
        if (p2 - p1 >= 3) sort(p1 + 1, p2 - 1)
        if (to - p2 >= 2) sort(p2 + 1, to)
    }

    fun partition(from: Int, to: Int): Pair<Int, Int> {
        if (endTime[from] > endTime[to]) swap(from, to)
        val lPivot = endTime[from]
        val rPivot = endTime[to]
        var lPoint = from
        var rPoint = to
        var i = from + 1
        while (i != rPoint) {
            val curr = endTime[i]
            if (curr < lPivot) {
                swap(++lPoint, i++)
            } else if (curr > rPivot) {
                swap(--rPoint, i)
            } else {
                i++
            }
        }
        swap(from, lPoint)
        swap(to, rPoint)
        return lPoint to rPoint
    }

    fun swap(i1: Int, i2: Int) {
        var tmp = startTime[i1]
        startTime[i1] = startTime[i2]
        startTime[i2] = tmp
        tmp = endTime[i1]
        endTime[i1] = endTime[i2]
        endTime[i2] = tmp
        tmp = profit[i1]
        profit[i1] = profit[i2]
        profit[i2] = tmp
    }
}