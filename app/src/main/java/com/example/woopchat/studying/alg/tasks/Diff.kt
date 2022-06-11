package com.example.woopchat.studying.alg.tasks

//val prevArr = arrayOf(1, 2, 3, 4, 9, 5)
//val currArr = arrayOf(2, 3, 1, 7, 5, 4)

val prevArr = arrayOf("a", "b", "c", "d")
//val currArr = arrayOf("c", "b", "f", "a", "e")
//val currArr = arrayOf("a", "e", "b", "c", "f")
//val currArr = arrayOf("a", "b", "c", "f", "e")
//val currArr = arrayOf("b", "c", "d", "e", "a")
val currArr = arrayOf("d", "c", "b", "a")


val prevSet = hashSetOf(*prevArr)
val currSet = hashSetOf(*currArr)

fun diff() {
    val added = hashMapOf<String, Int>()
    val deleted = hashMapOf<String, Int>()

    var currInd = 0
    var prevInd = 0

    val moved = hashMapOf<String, Int>() //el to ind
    while (currInd != currSet.size || prevInd != prevSet.size) {
        val curr = currArr.getOrNull(currInd)
        val prev = prevArr.getOrNull(prevInd)
        if (prev != null && !currSet.contains(prev)) {
            deleted[prev] = prevInd
            prevInd++
        } else if (curr != null && !prevSet.contains(curr)) {
            added[curr] = currInd
            currInd++
        } else if (curr != prev) {
            if (prev != null && prev in moved) {
                moved[prev] = moved.getValue(prev) - prevInd + deleted.size
                prevInd++
            } else if (curr != null) {
                moved[curr] = currInd
                currInd++
            }
        } else {
            prevInd++
            currInd++
        }
    }

    println("currInd: $currInd")
    println("prevInd: $prevInd")

    println("deleted: $deleted")
    println("moved: $moved")
    println("added: $added")
}
