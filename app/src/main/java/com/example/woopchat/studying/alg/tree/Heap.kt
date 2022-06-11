package com.example.woopchat.studying.alg.tree

class Heap(
    private val backed: ArrayList<Int> = arrayListOf(),
) {
    fun sort() {
        val n = backed.size

        // Build heap (rearrange array)
        for (i in n / 2 - 1 downTo 0) heapify(i)
    }

    fun heapify(i: Int) {
        val size: Int = backed.size
        var largest = i
        val l = 2 * i + 1
        val r = 2 * i + 2
        if (l < size && backed[l] > backed[largest]) largest = l
        if (r < size && backed[r] > backed[largest]) largest = r
        if (largest != i) {
            val temp = backed[largest]
            backed[largest] = backed[i]
            backed[i] = temp
            heapify(largest)
        }
    }

    fun insert(new: Int) {
        if (backed.isEmpty()) {
            backed[0] = new
        } else {
            backed[backed.size] = new

            //bubble up in the heap
            var k = backed.lastIndex
            while(k > 0 && backed[k] > backed[(k - 1) / 2]) {
                val tmp = backed[k]
                backed[k] = backed[(k - 1) / 2]
                backed[(k - 1) / 2] = tmp
                k = (k - 1) / 2
            }
        }
    }

    fun delete(delete: Int) {
        val size: Int = backed.size
        var i = 0
        while (i < size) {
            if (delete == backed[i]) break
            i++
        }
        val temp = backed[i]
        backed[i] = backed[size - 1]
        backed[size - 1] = temp
        backed.remove(size - 1)
        for (j in size / 2 - 1 downTo 0) heapify(j)
    }

    override fun toString(): String {
        return backed.joinToString()
    }
}

fun mainHeapify() {
    val heap = Heap(arrayListOf(2, 1, 6, 4, 5, 12, 8))
    println(heap)
    heap.delete(5)
    println(heap)
}

fun removeElement(arr: Array<out Any>, removedIdx: Int) {
    System.arraycopy(arr, removedIdx + 1, arr, removedIdx, arr.size - 1 - removedIdx)
}
