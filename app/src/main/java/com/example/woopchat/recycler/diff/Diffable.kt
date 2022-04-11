package com.example.woopchat.recycler.diff

interface Diffable : Identifiable<Any> {
    override val id: Any
        get() = javaClass

    fun isContentTheSame(old: Diffable): Boolean {
        return equals(old)
    }

    fun getChangePayload(old: Diffable): Any? {
        return null
    }
}

interface Identifiable<out T> {
    val id: T
}