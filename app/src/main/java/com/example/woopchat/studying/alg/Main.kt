package com.example.woopchat.studying.alg

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.PriorityQueue
import kotlin.math.abs




private val inNodes = listOf("0 0","2 0","0 2","2 2",)
private val inN = inNodes.size
private val inLimit = 1
private val inStartEnd = "1 4"

fun main() {
    val n = readLine()!!.toInt()
    val str = CharArray(n * 2) { ' ' }
    val points = IntArray(n) { 0 }
    iter(0, 0, points, n, str)
}

fun iter(pi: Int, s: Int, points: IntArray, n: Int, str: CharArray) {
    val e = pi * 2
    for (i in s..e) {
        points[pi] = i
        if (pi == n - 1) {
            printStr(points, str)
        } else {
            iter(pi + 1, i + 1, points, n, str)
        }
    }
}

fun printStr(points: IntArray, str: CharArray) {
    var pi = 0
    for (i in str.indices) {
        if (pi < points.size && points[pi] == i) {
            str[i] = '('
            pi++
        } else str[i] = ')'
    }
    println(String(str))
}

