package com.example.woopchat.studying.alg.tasks

class Solution2 {
    val sb = StringBuilder()

    lateinit var minPal: Array<IntArray>
    lateinit var makePals: Array<IntArray>

    fun palindromePartition(s: String, k: Int): Int {
        minPal = Array(k) { IntArray(s.length) { -1 } }
        makePals = Array(s.length) { IntArray(s.length) { -1 } }
//        makePals2 = HashMap()
        return minPalindrome(s, 0, k - 1)
    }

    fun minPalindrome(s: String, start: Int, k: Int): Int {
        if (k == 0) return makePalindrome(s, start, s.lastIndex)
        if (minPal[k][start] != -1) return minPal[k][start]
        var min = Int.MAX_VALUE
        for (end in start..s.lastIndex - k) {
            min = minOf(min, makePalindrome(s, start, end) + minPalindrome(s, end + 1, k - 1))
        }
        minPal[k][start] = min
        return min
    }

    fun makePalindrome(s: String, from: Int, to: Int): Int {
        if (to == from) return 0
//        if (makePals[from][to] != -1) return makePals[from][to]
        val changes = (if (s[from] == s[to]) 0 else 1) + if (to - from >= 3) makePalindrome(s, from +  1, to - 1) else 0
//        makePals[from][to] = changes
        return changes
    }

    fun printStr(s: String, inds: IntArray): String {
        sb.clear()
        for (i in 0 until inds.lastIndex) {
            val curr = inds[i]
            val next = inds[i + 1]
            sb.append(s.substring(curr, next))
            sb.append(" ")
        }
        return sb.toString().also(::print)
    }
}