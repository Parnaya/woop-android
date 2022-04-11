package com.example.woopchat.recycler

import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.core.util.forEach
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.woopchat.recycler.diff.Diffable
import com.example.woopchat.recycler.holder.SimpleViewHolder

class DelegatingAdapter constructor(
    items: List<Diffable>,
    private val delegates: SparseArray<AdapterDelegate>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<Diffable> = items
        private set

    val lastPosition: Int
        get() = itemCount - 1

    val states = Bundle()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //nothing
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == UNKNOWN_VIEW_TYPE) {
            return SimpleViewHolder(View(parent.context))
        }
        return delegates[viewType].createHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        val itemToBind = items[position]
        holder.itemView.setBound(itemToBind)
        delegates[holder.itemViewType]?.apply {
            bindHolder(holder, items, position, payloads)
            if (isStateful) {
                val key = itemToBind.id.toString()
                states.getParcelable<Parcelable?>(key)?.let { state ->
                    restoreState(holder, state)
                }
                states.remove(key)
            }
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        delegates[holder.itemViewType]?.onAttachToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        delegates[holder.itemViewType]?.apply {
            onDetachFromWindow(holder)
            if (isStateful) {
                saveState(this, holder)
            }
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        delegates[holder.itemViewType]?.onRecycled(holder)
    }

    override fun getItemViewType(position: Int): Int {
        delegates.forEach { viewType, adapter ->
            if (adapter.isSupported(items, position)) {
                return viewType
            }
        }
        return UNKNOWN_VIEW_TYPE
    }

    fun getItemViewType(delegateClass: Class<out AdapterDelegate>): Int {
        delegates.forEach { index, delegate ->
            if (delegate::class.java == delegateClass) return index
        }
        return UNKNOWN_VIEW_TYPE
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        delegates.forEach { _, delegate -> delegate.onAttachedToRecyclerView(recyclerView) }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        delegates.forEach { _, delegate -> delegate.onDetachedFromRecyclerView(recyclerView) }
    }

    fun setItems(newItems: List<Diffable>, diff: DiffUtil.DiffResult? = null) {
        if (items === newItems) {
            return
        }
        items = newItems
        if (diff != null) {
            diff.dispatchUpdatesTo(this)
        } else {
            notifyDataSetChanged()
        }
    }

    fun addItems(itemsToAdd: List<Diffable>, atPosition: Int = itemCount) {
        if (itemsToAdd.isEmpty()) {
            return
        }
        val oldItems = items
        val newItems = oldItems + itemsToAdd
        items = newItems
        notifyItemRangeInserted(atPosition, itemsToAdd.size)
    }

    fun addItem(itemToAdd: Diffable, atPosition: Int = itemCount) {
        val oldItems = items
        val newItems = oldItems + itemToAdd
        items = newItems
        notifyItemInserted(atPosition)
    }

    fun saveState(holder: RecyclerView.ViewHolder) {
        delegates[holder.itemViewType]?.apply {
            if (isStateful) {
                saveState(this, holder)
            }
        }
    }

    fun restoreState(state: Parcelable) {
        if (state is Bundle) {
            states.putAll(state.also { it.classLoader = javaClass.classLoader })
        }
    }

    private fun saveState(delegate: AdapterDelegate, holder: RecyclerView.ViewHolder) {
        delegate.apply {
            holder.itemView.getBound<Diffable>()?.let { item ->
                val key = item.id.toString()
                val state = saveState(holder)
                if (state != null) {
                    states.putParcelable(key, state)
                } else {
                    states.remove(key)
                }
            }
        }
    }

    companion object {
        private const val UNKNOWN_VIEW_TYPE = -1

        fun create(
            vararg delegates: AdapterDelegate
        ): DelegatingAdapter {
            return DelegatingAdapter(
                items = listOf(),
                delegates = SparseArray<AdapterDelegate>(delegates.size).apply {
                    delegates.forEachIndexed { index, adapter -> append(index, adapter) }
                }
            )
        }
    }
}
