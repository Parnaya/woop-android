package com.example.woopchat.studying.alg.tree

import kotlin.math.abs
import kotlin.math.max

//The properties that separate a binary search tree from a regular binary tree is
//
//- All nodes of left subtree are less than the root node
//- All nodes of right subtree are more than the root node
//- Both subtrees of each node are also BSTs i.e. they have the above two properties
class BST(array: Array<Int>): Node<Int>(array[0]) {

    init {
        array.toBinaryTree(this)
    }

    fun get(el: Int): Node<Int>? {
        return getRec(this, el)
    }

    fun getRec(node: Node<Int>, el: Int): Node<Int>? {
        if (node.value == el) return node
        return node.left?.let { getRec(it, el) } ?: node.right?.let { getRec(it, el) }
    }

    fun add(el: Int) {
        addRec(this, el)
    }

    private fun addRec(node: Node<Int>, el: Int) {
        if (el > node.value) {
            val r = node.right
            if (r == null) node.right = Node(el) else addRec(r, el)
        } else {
            val l = node.left
            if (l == null) node.left = Node(el) else addRec(l, el)
        }
    }

    private fun getLeftMost(node: Node<Int>): Node<Int> {
        return node.left?.let(::getLeftMost) ?: node
    }
}