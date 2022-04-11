package com.example.woopchat.recycler

import android.content.Context
import android.graphics.Color
import android.util.SparseArray
import androidx.core.util.size
import com.example.woopchat.R

fun AdapterWithVerticalDividers(context: Context, vararg delegates: AdapterDelegate): DelegatingAdapter {
    return DelegatingAdapter(
        items = listOf(),
        delegates = SparseArray<AdapterDelegate>(delegates.size).apply {
            delegates.forEachIndexed { index, adapter -> append(index, adapter) }
            append(size, DividerAdapter(context))
        }
    )
}

fun AdapterWithHorizontalDividers(context: Context, vararg delegates: AdapterDelegate): DelegatingAdapter {
    return DelegatingAdapter(
        items = listOf(),
        delegates = SparseArray<AdapterDelegate>(delegates.size).apply {
            delegates.forEachIndexed { index, adapter -> append(index, adapter) }
            append(size, DividerAdapter(context, DividerAdapter.Horizontal))
        }
    )
}

fun DividerAdapter(context: Context, impl: DividerAdapter.Impl = DividerAdapter.Vertical): DividerAdapter {
    return DividerAdapter(Color.TRANSPARENT, impl)
}