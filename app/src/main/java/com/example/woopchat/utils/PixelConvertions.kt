package com.example.woopchat.base

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.fragment.app.Fragment

fun Resources.pxi(dp: Float): Int {
    return (0.5f + displayMetrics.density * dp).toInt()
}

fun Resources.pxi(dp: Int): Int {
    return (0.5f + displayMetrics.density * dp).toInt()
}

fun Resources.pxf(dp: Float): Float {
    return displayMetrics.density * dp
}

fun Resources.pxf(dp: Int): Float {
    return displayMetrics.density * dp
}

fun Resources.spToPxf(sp: Int): Float {
    return displayMetrics.scaledDensity * sp
}

fun Context.pxi(dp: Float): Int {
    return resources.pxi(dp)
}

fun Context.pxi(dp: Int): Int {
    return resources.pxi(dp)
}

fun Context.pxf(dp: Float): Float {
    return resources.pxf(dp)
}

fun Context.pxf(dp: Int): Float {
    return resources.pxf(dp)
}

fun Fragment.pxi(dp: Float): Int {
    return resources.pxi(dp)
}

fun Fragment.pxi(dp: Int): Int {
    return resources.pxi(dp)
}

fun Fragment.pxf(dp: Float): Float {
    return resources.pxf(dp)
}

fun Fragment.pxf(dp: Int): Float {
    return resources.pxf(dp)
}

fun View.pxi(dp: Float): Int {
    return resources.pxi(dp)
}

fun View.pxi(dp: Int): Int {
    return resources.pxi(dp)
}

fun View.pxf(dp: Float): Float {
    return resources.pxf(dp)
}

fun View.pxf(dp: Int): Float {
    return resources.pxf(dp)
}

fun Resources.dpi(px: Float): Int {
    return (0.5f + px / displayMetrics.density).toInt()
}

fun Resources.dpi(px: Int): Int {
    return (0.5f + px / displayMetrics.density).toInt()
}

fun Resources.dpf(px: Float): Float {
    return px / displayMetrics.density
}

fun Resources.dpf(px: Int): Float {
    return px / displayMetrics.density
}

fun Context.dpi(px: Float): Int {
    return resources.dpi(px)
}

fun Context.dpi(px: Int): Int {
    return resources.dpi(px)
}

fun Context.dpf(px: Float): Float {
    return resources.dpf(px)
}

fun Context.dpf(px: Int): Float {
    return resources.dpf(px)
}

fun Fragment.dpi(px: Float): Int {
    return resources.dpi(px)
}

fun Fragment.dpi(px: Int): Int {
    return resources.dpi(px)
}

fun Fragment.dpf(px: Float): Float {
    return resources.dpf(px)
}

fun Fragment.dpf(px: Int): Float {
    return resources.dpf(px)
}

fun View.dpi(px: Float): Int {
    return resources.dpi(px)
}

fun View.dpi(px: Int): Int {
    return resources.dpi(px)
}

fun View.dpf(px: Float): Float {
    return resources.dpf(px)
}

fun View.dpf(px: Int): Float {
    return resources.dpf(px)
}

fun View.spToPxf(sp: Int): Float {
    return resources.spToPxf(sp)
}