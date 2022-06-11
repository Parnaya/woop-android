package com.example.woopchat.studying.alg.tasks

class Solution {
    fun findTarget(root: TreeNode, k: Int): Boolean {
        return traverse(root, k, HashSet())
    }

    fun traverse(curr: TreeNode?, k: Int, set: HashSet<Int>): Boolean {
        if (curr == null) return false
        if (set.contains(k - curr.`val`)) return true
        set.add(curr.`val`)

        return traverse(curr.left, k, set) || traverse(curr.right, k, set)
    }
}