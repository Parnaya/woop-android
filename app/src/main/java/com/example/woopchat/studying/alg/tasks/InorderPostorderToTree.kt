package com.example.woopchat.studying.alg.tasks

class InorderPostorderToTree {
    class TreeNode(var `val`: Int) {
        var left: TreeNode? = null
        var right: TreeNode? = null
    }

    private lateinit var inorder: IntArray
    private lateinit var postorder: IntArray
    private var postInd = 0

    fun buildTree(inorder: IntArray, postorder: IntArray): TreeNode? {
        this.inorder = inorder
        this.postorder = postorder
        postInd = postorder.size
        return makeNode(0, postorder.lastIndex)
    }

    fun makeNode(from: Int, to: Int): TreeNode? {
        if (from > to) return null
        if (--postInd < 0) return null
        val node = TreeNode(postorder[postInd])
        if (to > from) {
            val ind = indexInorder(node.`val`, from, to)
            node.right = makeNode(ind + 1, to)
            node.left = makeNode(from, ind - 1)
        }
        return node
    }

    fun indexInorder(value: Int, from: Int, to: Int): Int {
        for (i in from..to) if (inorder[i] == value) return i
        return -1
    }
}
