package com.example.woopchat.studying.alg.tasks

import java.lang.StringBuilder

class Matrix<T>(
    val size: Int = 0,
    val create: (x: Int, y: Int) -> T,
) {
    val lastIndex = size - 1
    val values: MutableList<MutableList<T>> = MutableList(size) { y -> MutableList(size) { x ->  create(x, y) } }

    fun set(x: Int, y: Int, value: T) {
        values[y][x] = value
        println("===set: x = $x y = $y, value = $value")
    }

    fun get(x: Int, y: Int): T = values[y][x]

    private val sb: StringBuilder = StringBuilder()

    override fun toString(): String {
        sb.clear()
        for (y in 0..lastIndex) {
            for (x in 0..lastIndex) {
                sb.append("${get(x, y)}  ")
            }
            sb.appendLine()
        }
        return sb.toString()
    }
}

fun snake(size: Int = 7) {
    bases.clear()
    val matrix = Matrix(size) { x, y -> getSnakeValue(x = x, y = y, lastIndex = size - 1) }
    print(matrix)
}

private val bases: HashMap<Int, Int> = hashMapOf() //ind to offset

private fun getBase(min: Int, lastIndex: Int): Int {
    var value = bases[min]
    if (value != null) return value
    if (min == 0) {
        value = 0
        bases[min] = value
        return value
    }

    value = getBase(min - 1, lastIndex) + ((lastIndex - (min - 1) * 2) * 4)
    return value
}

private fun getSnakeValue(x: Int, y: Int, lastIndex: Int): Int {
    val min = minOf(x, y , lastIndex - x, lastIndex - y)
    val base = getBase(min, lastIndex)
    val offset = when {
        x >= y -> x + y - 2 * min
        else -> lastIndex * 4 - min * 6 - x - y
    }
    return base + offset
}

fun trans() {
    var i = 0
    val matrix = Matrix(6) { _, _ -> i++ }
    println(matrix)
    matrix.transpose()
    println(matrix)
}

fun <T> Matrix<T>.transpose() {
    var tmp: T
    for (y in 0..lastIndex / 2) {
        for (x in y..(lastIndex - y - 1)) {
            tmp = get(x, y)
            set(x, y, get(x = y, y = lastIndex - x))
            set(x = y, y = lastIndex - x, get(x = lastIndex - x, y = lastIndex - y))
            set(x = lastIndex - x, y = lastIndex - y, get(x = lastIndex - y, y = x))
            set(x = lastIndex - y, y = x, tmp)
        }
    }
}