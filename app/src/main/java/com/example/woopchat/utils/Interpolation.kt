package com.example.woopchat.utils

import android.animation.ArgbEvaluator
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator

object Easing {
    val standard = FastOutSlowInInterpolator()
    val accelerate = FastOutLinearInInterpolator()
    val decelerate = LinearOutSlowInInterpolator()
}

object Evaluator {
    val argb = ArgbEvaluator()
}

fun lerp(start: Float, end: Float, fraction: Float): Float {
    return (end - start) * fraction + start
}

fun lerp(start: Int, end: Int, fraction: Float): Float {
    return (end - start) * fraction + start
}

fun lerpColor(start: Int, end: Int, fraction: Float): Int {
    return Evaluator.argb.evaluate(fraction, start, end) as Int
}
