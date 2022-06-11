package com.example.woopchat.studying.alg.sort

class DualPivotQuickSort {
    fun sort(arr: IntArray, from: Int = 0, to: Int = arr.lastIndex) { //dual pivot sort
        val (p1, p2) = partition(arr, from, to)
        if (p1 - from >= 2) sort(arr, from, p1 - 1)
        if (p2 - p1 >= 3) sort(arr,p1 + 1, p2 - 1)
        if (to - p2 >= 2) sort(arr, p2 + 1, to)
    }

    fun partition(arr: IntArray, from: Int, to: Int): Pair<Int, Int> {
        if (arr[from] > arr[to]) swap(arr, from, to)
        val lPivot = arr[from]
        val rPivot = arr[to]
        var lPoint = from
        var rPoint = to
        var i = from + 1
        while (i != rPoint) {
            if (arr[i] < lPivot) {
                swap(arr, ++lPoint, i++)
            } else if (arr[i] > rPivot) {
                swap(arr, --rPoint, i)
            } else {
                i++
            }
        }
        swap(arr, from, lPoint)
        swap(arr, to, rPoint)
        return lPoint to rPoint
    }

    fun swap(arr: IntArray, i1: Int, i2: Int) {
        val tmp = arr[i1]
        arr[i1] = arr[i2]
        arr[i2] = tmp
    }
}