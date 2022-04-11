package com.example.woopchat.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

const val WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT
const val MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT

inline fun View.onMarginLayoutParams(block: ViewGroup.MarginLayoutParams.() -> Unit) {
    (layoutParams as? ViewGroup.MarginLayoutParams)?.apply(block)
}

val View.inflater: LayoutInflater get() = LayoutInflater.from(context)