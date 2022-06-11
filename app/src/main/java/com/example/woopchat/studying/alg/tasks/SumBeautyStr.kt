package com.example.woopchat.studying.alg.tasks

val letters = IntArray(26)

fun beautySum(s: String): Int {
    var acc = 0
    for (len in 2..s.length) {
        for (shift in 0..s.length - len) {
            acc += beauty(s, shift, shift + len)
        }
    }
    return acc
}

fun beauty(s: String, from: Int, to: Int): Int {
    var max = Int.MIN_VALUE
    var singleChar = true
    for (i in from until to) {
        val ch = s[i]
        val count = letters[ch - 'a'] + 1
        letters[ch - 'a'] = count
        if (count > max) max = count
        else singleChar = false
    }
    if (singleChar) {
        letters[s[from] - 'a'] = 0
        return 0
    }
    val min = minAndClear()
    return max - min
}

fun minAndClear(): Int {
    var min = Int.MAX_VALUE
    for (i in letters.indices) {
        if (letters[i] != 0 && letters [i] < min) {
            min = letters[i]
            letters[i] = 0
        }
    }
    return min
}