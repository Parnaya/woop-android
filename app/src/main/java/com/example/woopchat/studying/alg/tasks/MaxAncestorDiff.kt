package com.example.woopchat.studying.alg.tasks


class TreeNode(var `val`: Int) {
    var left: TreeNode? = null
    var right: TreeNode? = null
}

var res = 0

fun maxAncestorDiff(root: TreeNode?): Int {
    res = 0
    root?.let { traverse(root, Int.MAX_VALUE, Int.MIN_VALUE) }
    return res
}

fun traverse(root: TreeNode, min: Int, max: Int) {
    val nMax = maxOf(max, root.`val`)
    val nMin = minOf(min, root.`val`)

    res = maxOf(res, nMax - nMin)

    root.left?.let { traverse(it, nMin, nMax) }
    root.right?.let { traverse(it, nMin, nMax) }
}

fun find(root: TreeNode, value: Int): Boolean {
    return root.`val` == value
           || root.right?.let { find(it, value) } ?: false
           || root.left?.let { find(it, value) } ?: false
}