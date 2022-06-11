package com.example.woopchat.studying.alg.tree

import kotlin.math.abs
import kotlin.math.max


class AvlNode(
    val key: Int,
) {
    /**
     * Balance Factor = (Height of Left Subtree - Height of Right Subtree)
     */
    var height: Int = 0 //or height?
    val balanceFactor: Int
        get() = (left?.height ?: -1) - (right?.height ?: -1)

    var left: AvlNode? = null
    var right: AvlNode? = null
}

class AvlTree {
    var root: AvlNode? = null

    /**        p                     p
     *        |                     |
     *       (X)                   (Y)
     *      /   \       =>        /   \
     *     a    (Y)             (X)    c
     *         /   \           /   \
     *        b     c         a     b
     *
     * g(f(x)) = f(g(x)), where g and f right and left rotation functions, accordingly
     */
    private fun leftRotate(x: AvlNode): AvlNode {
        val y = x.right ?: return x
        val b = y.left
        y.left = x
        x.right = b
        x.height = max(x.left?.height ?: -1, x.right?.height ?: -1) + 1
        y.height = max(y.left?.height ?: -1, y.right?.height ?: -1) + 1
        return y
    }

    /**           p                  p
     *           |                  |
     *          (Y)                (X)
     *         /   \      =>      /   \
     *       (X)    c            a    (Y)
     *      /   \                    /   \
     *     a     b                  b     c
     *
     * g(f(x)) = f(g(x)), where g and f right and left rotation functions, accordingly
     */
    private fun rightRotate(y: AvlNode): AvlNode {
        val x = y.left ?: return y
        val b = x.right
        x.right = y
        y.left = b
        x.height = max(x.left?.height ?: -1, x.right?.height ?: -1) + 1
        y.height = max(y.left?.height ?: -1, y.right?.height ?: -1) + 1
        return x
    }

    fun insert(key: Int) {
        root = insertRec(root, key)
    }

    private fun insertRec(node: AvlNode?, key: Int): AvlNode {
        if (node == null) return AvlNode(key)
        if (key < node.key) {
            node.left = insertRec(node.left, key)
        } else {
            node.right = insertRec(node.right, key)
        }
        node.height = max(node.left?.height ?: -1, node.right?.height ?: -1) + 1
        return node.ensureBalance(key)
    }


    /**
     *         (Z)
     *        /   \
     *      (Y)    d
     *     /   \
     *    a    (X)
     *        /   \
     *       b     c
     * some text
     */
    private fun AvlNode.ensureBalance(insertedKey: Int): AvlNode {
        val z = this
        return when {
            z.balanceFactor > 1 -> { //was added to left
                val y = z.left!!
                when {
                    insertedKey < y.key -> { //left-left
                        rightRotate(z)
                    }
                    else -> { //left-right
                        z.left = leftRotate(y)
                        rightRotate(z)
                    }
                }
            }
            z.balanceFactor < -1 -> { //was added to right
                val y = z.right!!
                when {
                    insertedKey < y.key -> { //right-left
                        z.left = rightRotate(y)
                        leftRotate(z)
                    }
                    else -> { //right-right
                        leftRotate(z)
                    }
                }
            }
            else -> z
        }
    }
}