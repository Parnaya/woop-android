package com.example.woopchat.recycler

import android.os.Parcelable
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.woopchat.recycler.diff.Diffable

interface AdapterDelegate {
    val isStateful: Boolean
        get() = false

    fun isSupported(items: List<Diffable>, position: Int): Boolean
    fun createHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun bindHolder(holder: RecyclerView.ViewHolder, items: List<Diffable>, position: Int, payloads: List<Any>)
    fun onAttachToWindow(holder: RecyclerView.ViewHolder) {}
    fun onDetachFromWindow(holder: RecyclerView.ViewHolder) {}
    fun onRecycled(holder: RecyclerView.ViewHolder) {}
    fun onAttachedToRecyclerView(recyclerView: RecyclerView) {}
    fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {}
    fun saveState(holder: RecyclerView.ViewHolder): Parcelable? = null
    fun restoreState(holder: RecyclerView.ViewHolder, state: Parcelable) {}
}
