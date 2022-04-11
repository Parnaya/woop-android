package com.example.woopchat.recycler

import androidx.recyclerview.widget.RecyclerView
import com.example.woopchat.recycler.diff.Diffable

@Suppress("UNCHECKED_CAST")
abstract class TypedAdapterDelegate<T>(
    private val cls: Class<T>
) : AdapterDelegate {

    open fun bindHolder(holder: RecyclerView.ViewHolder, item: T, payloads: List<Any>) {}

    open fun isSupported(item: T): Boolean {
        return true
    }

    override fun bindHolder(holder: RecyclerView.ViewHolder, items: List<Diffable>, position: Int, payloads: List<Any>) {
        bindHolder(holder, items[position] as T, payloads)
    }

    final override fun isSupported(items: List<Diffable>, position: Int): Boolean {
        val item = items[position]
        return cls.isInstance(item) && isSupported(item as T)
    }
}
