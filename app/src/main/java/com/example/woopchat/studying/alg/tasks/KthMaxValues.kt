package com.example.woopchat.studying.alg.tasks

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*
import kotlin.math.min

// input: [1, 3, 1, 2, 5, 7, 3, 5, 6, 3] k = 3
// output: [3, 5, 1]

@RequiresApi(Build.VERSION_CODES.N)
fun kMaxValues(nums: IntArray, k: Int): IntArray {
    val countMap = mutableMapOf<Int, Int>()
    nums.forEach { num ->
        countMap[num] = (countMap[num] ?: 0) + 1
    }


    val pq = PriorityQueue<Pair<Int, Int>>(compareBy { it.second })
    //At the head stores least element
    val priorityQueue = PriorityQueue<Pair<Int, Int>> { o1, o2 ->
        when {
            o1.second > o2.second -> -1
            o1.second > o2.second -> 0
            else -> 1
        }
    }

    countMap.forEach { (num, count) ->
        priorityQueue.add(num to count)
    }

    val total = IntArray(k)
    for (i in 0 until min(k, priorityQueue.size)) {
        total[i] = priorityQueue.remove().first
    }

    return total
}