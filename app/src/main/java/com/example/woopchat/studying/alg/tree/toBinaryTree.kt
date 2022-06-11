package com.example.woopchat.studying.alg.tree

fun mainToBinaryTree() {
//    val hh: Node<Int> = Array(17) { it }.toBinaryTree()
    val hh = Node(0)
    hh.left = Node(1)
    hh.right = Node(2)
    hh.left?.left = Node(3)
    hh.left?.right = Node(8)
    hh.left?.left?.left = Node(3)
//    hh.left?.right = Node(4)
//    hh.right?.left = Node(5)
    hh.right?.right = Node(6)
//    hh.right?.right?.right = Node(6)
    print(hh.isBalanced())
}

fun <T> Array<T>.toBinaryTree(root: Node<T> = Node(first()), ind: Int = 0): Node<T> {
    val lInd = ind * 2 + 1
    val rInd = ind * 2 + 2

    if (lInd < size) root.left = toBinaryTree(Node(get(lInd)), lInd)
    if (rInd < size) root.right = toBinaryTree(Node(get(rInd)), rInd)
    return root
}

