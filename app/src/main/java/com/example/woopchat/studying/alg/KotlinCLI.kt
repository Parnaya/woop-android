package com.example.woopchat.studying.alg

import java.io.Closeable
import java.io.File
import java.io.PrintWriter
import java.util.*

/**
 * https://stackoverflow.com/questions/41283393/reading-console-input-in-kotlin
 * https://kotlinlang.org/docs/competitive-programming.html#more-tips-and-tricks
 */

private fun readln() = readLine()!!

private fun <T : Closeable, R> T.useWith(block: T.() -> R): R = use { with(it, block) }

private fun fileExamples() {
    File("a.in").bufferedReader().useWith {
        File("a.out").printWriter().useWith {
            val (a, b) = readLine()!!.split(' ').map(String::toInt)
            println(a + b)
        }
    }

    Scanner(File("b.in")).useWith {
        PrintWriter("b.out").useWith {
            val a = nextInt()
            val b = nextInt()
            println(a + b)
        }
    }
}