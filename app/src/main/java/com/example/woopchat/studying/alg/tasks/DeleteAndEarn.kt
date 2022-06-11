package com.example.woopchat.studying.alg.tasks

val numCount = HashMap<Int, Int>()

fun deleteAndEarn(nums: IntArray): Int {
    nums.forEach { num ->
        numCount[num] = (numCount[num] ?: 0) + num
    }

    var acc = 0

    numCount.forEach fe@{ (num, _) ->
        if (numCount[num - 1] != null) return@fe
        acc += max(num)
    }

    return acc
}

val numMax = HashMap<Int, Int>()

fun max(num: Int): Int {
    if (numCount[num] == null) return 0
    if (numMax[num] != null) return numMax[num]!!
    val take = if (numCount[num + 1] != null) max(num + 2) + numCount[num]!! else numCount[num]!!
    val noTake = max(num + 1)
    val max = maxOf(take, noTake)
    numMax[num] = max
    return max
}

fun max2(num: Int): Int {
    var a: Int = 0
    var b: Int = 0

    var shift = 0
    while (numCount[num + shift] != null) {
        val tmp = maxOf(a + (numCount[num + shift] ?: 0), b)
        a = b
        b = tmp
        shift++
    }
    return b
}


