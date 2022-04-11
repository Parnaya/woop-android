package com.example.woopchat.recycler.diff

import androidx.recyclerview.widget.DiffUtil

class DiffRequest<T : Diffable>(
    var old: List<T>,
    var new: List<T>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return new.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return new[newItemPosition].id == old[oldItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return new[newItemPosition].isContentTheSame(old[oldItemPosition])
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return new[newItemPosition].getChangePayload(old[oldItemPosition])
    }
}
