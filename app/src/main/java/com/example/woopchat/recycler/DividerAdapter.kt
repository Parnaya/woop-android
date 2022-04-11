package com.example.woopchat.recycler

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.woopchat.utils.MATCH_PARENT
import com.example.woopchat.utils.onMarginLayoutParams
import com.example.woopchat.base.pxi
import com.example.woopchat.recycler.model.Divider
import com.example.woopchat.recycler.holder.SimpleViewHolder

class DividerAdapter(
    private val color: Int,
    private val impl: Impl = Vertical
) : TypedAdapterDelegate<Divider>(Divider::class.java) {

    override fun createHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return SimpleViewHolder(
            View(parent.context).apply {
                layoutParams = impl.createParams(parent as RecyclerView)
            }
        )
    }

    override fun bindHolder(holder: RecyclerView.ViewHolder, item: Divider, payloads: List<Any>) {
        holder.itemView.apply {
            setBackgroundColor(if (item.colored) color else Color.TRANSPARENT)
            onMarginLayoutParams {
                impl.setSize(this, pxi(item.size))
                leftMargin = pxi(item.marginLeft)
                topMargin = pxi(item.marginTop)
                rightMargin = pxi(item.marginRight)
                bottomMargin = pxi(item.marginBottom)
            }
        }
    }

    interface Impl {
        fun createParams(parent: RecyclerView): RecyclerView.LayoutParams
        fun setSize(params: ViewGroup.MarginLayoutParams, size: Int)
    }

    object Vertical : Impl {
        override fun createParams(parent: RecyclerView): RecyclerView.LayoutParams {
            return RecyclerView.LayoutParams(MATCH_PARENT, 0)
        }

        override fun setSize(params: ViewGroup.MarginLayoutParams, size: Int) {
            params.height = size
        }
    }

    object Horizontal : Impl {
        override fun createParams(parent: RecyclerView): RecyclerView.LayoutParams {
            return RecyclerView.LayoutParams(0, MATCH_PARENT)
        }

        override fun setSize(params: ViewGroup.MarginLayoutParams, size: Int) {
            params.width = size
        }
    }
}
