package com.example.woopchat.studying.alg

fun quickSort(arr: IntArray, from: Int = 0, to: Int = arr.size - 1): IntArray {

    val pivot = partition(arr, from, to)

    if (pivot - from > 1) quickSort(arr, from, pivot - 1)
    if (to - pivot > 1) quickSort(arr, pivot + 1, to)

    return arr
}

private fun partition(arr: IntArray, from: Int, to: Int): Int {
    val pivot = arr[to]

    var left: Int = from - 1
    var right: Int = from
    while(right <= to) {
//        if (arr[right] <= pivot && left + 1 != right) {
        if (arr[right] <= pivot) {
            swap(arr, ++left, right)
        }
        right++
    }
    return left
}

private fun swap(arr: IntArray, i: Int, j: Int) {
    val temp = arr[i]
    arr[i] = arr[j]
    arr[j] = temp
}