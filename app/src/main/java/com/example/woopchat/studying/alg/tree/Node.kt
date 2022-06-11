package com.example.woopchat.studying.alg.tree

import kotlin.math.abs
import kotlin.math.max

open class Node<T>(
    val value: T,
) {
    var left: Node<T>? = null
    var right: Node<T>? = null

    override fun toString(): String {
        val builder = StringBuilder()
        builder.appendNode(this)
        return builder.toString()
    }
}

fun <T> StringBuilder.appendNode(node: Node<T>, offset: Int = 0) {
    node.right?.let { appendNode(it, offset + Count) }
    repeat(offset) { append(" ") }
    append(node.value.toString())
    appendLine()
    node.left?.let { appendNode(it, offset + Count) }
}

private val Count = 4


//              6
//        /            \
//      1               4
//   /     \         /     \
//  4       6       4       6
// / \     / \     / \     / \
//5   6   7   8   5   6   7   8

fun <T> Node<T>.isBalanced(): Boolean {
    return isBalancedAndHeight().first
}

fun <T> Node<T>.height(parentHeight: Int = 0): Int {
    val height = parentHeight + 1
    return max(left?.height(height) ?: height, right?.height(height) ?: height)
}

fun <T> Node<T>.isBalancedAndHeight(parentHeight: Int = 0): Pair<Boolean, Int> {
    val height = parentHeight + 1
    val (isLeftBalanced, leftHeight) = left?.isBalancedAndHeight(height) ?: true to height
    val (isRightBalanced, rightHeight) = right?.isBalancedAndHeight(height) ?: true to height
    if (abs(leftHeight - rightHeight) > 1) return false to height
    return (isLeftBalanced || isRightBalanced) to max(leftHeight, rightHeight)
}