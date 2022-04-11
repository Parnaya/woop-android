package com.example.woopchat.recycler

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.woopchat.coroutines.Dispatchers
import com.example.woopchat.recycler.diff.DiffRequest
import com.example.woopchat.recycler.diff.Diffable
import kotlinx.coroutines.withContext

fun RecyclerView.addItems(items: List<Diffable>) {
    val adapter = getDelegatingAdapter()
    adapter.addItems(items)
}

suspend fun RecyclerView.setItems(curr: List<Diffable>) {
    val adapter = getDelegatingAdapter()
    val prev = adapter.items
    when {
        adapter.items.isEmpty() -> adapter.addItems(curr)
        prev !== curr -> {
            val diff = withContext(Dispatchers.io) {
                DiffUtil.calculateDiff(DiffRequest(prev, curr))
            }
            adapter.setItems(curr, diff)
        }
    }
}

fun RecyclerView.getDelegatingAdapter(): DelegatingAdapter {
    return adapter as DelegatingAdapter
}