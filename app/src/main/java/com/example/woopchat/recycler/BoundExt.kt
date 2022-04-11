package com.example.woopchat.recycler

import android.view.View
import com.example.woopchat.R

fun View.setBound(bound: Any?) {
    setTag(R.id.tag_bound, bound)
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> View.getBound(): T? {
    return getTag(R.id.tag_bound) as? T
}