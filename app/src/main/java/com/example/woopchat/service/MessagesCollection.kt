package com.example.woopchat.service

import java.util.*
import kotlin.collections.HashMap

class MessagesCollection<K, V>(
    private val map: HashMap<K, V> = HashMap(),
    private val order: LinkedList<K> = LinkedList(),
    private val keyFromValue: (V) -> K,
): List<V> {

    fun last(): V = map.getValue(order.last)

    fun putAll(list: List<V>) = putAll(order.size, list)

    fun putAll(index: Int, list: List<V>) {
        var position = index
        list.forEach { value ->
            val key = keyFromValue(value)
            if (!map.containsKey(key)) {
                order.add(position, key)
                position++
            }
            map[key] = value
        }
    }

    fun put(value: V) = put(order.size, value)

    fun put(index: Int, value: V) {
        val key = keyFromValue(value)
        if (!map.containsKey(key)) {
            order.add(index, key)
        }
        map[key] = value
    }

    override val size: Int
        get() = order.size

    override fun contains(element: V): Boolean {
        return map.containsKey(keyFromValue(element))
    }

    override fun containsAll(elements: Collection<V>): Boolean {
        elements.forEach {
            if (!contains(it)) return false
        }
        return true
    }

    override fun get(index: Int): V {
        return map.getValue(order[index])
    }

    override fun indexOf(element: V): Int {
        val key = keyFromValue(element)
        return order.indexOf(key)
    }

    override fun isEmpty(): Boolean {
        return order.isEmpty()
    }

    override fun iterator(): kotlin.collections.Iterator<V> {
        return Iterator(order.listIterator())
    }

    override fun lastIndexOf(element: V): Int {
        return indexOfLast { keyFromValue(it) == keyFromValue(element) }
    }

    override fun listIterator(): ListIterator<V> {
        return Iterator(order.listIterator())
    }

    override fun listIterator(index: Int): ListIterator<V> {
        return Iterator(order.listIterator(index))
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<V> {
        val mutableList = mutableListOf<V>()
        val iterator = listIterator(fromIndex - 1)
        while (iterator.hasNext() && iterator.nextIndex() > toIndex) {
            mutableList.add(iterator.next())
        }
        return mutableList
    }

    private inner class Iterator(
        val orderIterator: ListIterator<K>
    ): ListIterator<V> {
        override fun hasNext(): Boolean {
            return orderIterator.hasNext()
        }

        override fun hasPrevious(): Boolean {
            return orderIterator.hasPrevious()
        }

        override fun next(): V {
            val key = orderIterator.next()
            return map.getValue(key)
        }

        override fun nextIndex(): Int {
            return orderIterator.nextIndex()
        }

        override fun previous(): V {
            val key = orderIterator.previous()
            return map.getValue(key)
        }

        override fun previousIndex(): Int {
            return orderIterator.previousIndex()
        }
    }
}