package com.example.woopchat.studying.alg.tasks

import java.util.*
import kotlin.collections.LinkedHashMap


fun maxLength(arr: List<String>): Int {
    return count(arr, 0, 0)
}

fun count(arr: List<String>, mask: Int, i: Int): Int {
    if (i == arr.size) return Integer.bitCount(mask)

    val msk = isValid(arr[i], mask)
    val take = if (msk == -1) -1 else count(arr, msk, i + 1)

    val notake = count(arr, mask, i + 1)

    return maxOf(take, notake)
}

fun isValid(str: String, mask: Int): Int {
    var new = mask
    for (i in str.indices) {
        val bit = 1 shl (str[i] - 'a')
        if (new and bit > 0) return -1
        else new = new or bit
    }
    return new
}