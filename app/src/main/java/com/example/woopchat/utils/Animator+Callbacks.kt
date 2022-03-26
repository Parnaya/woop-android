package com.example.woopchat.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import java.util.concurrent.atomic.AtomicReference

/**
 * Add an action which will be invoked if animation is ended successfully.
 */
inline fun Animator.doOnComplete(crossinline block: (Animator) -> Unit): AnimatorListenerAdapter {
    return object : AnimatorListenerAdapter() {
        var isCanceled = false

        override fun onAnimationCancel(animation: Animator?) {
            isCanceled = true
        }

        override fun onAnimationEnd(animation: Animator) {
            if (!isCanceled) {
                block.invoke(animation)
            }
        }
    }.also(::addListener)
}

/**
 * Add an action which will be once invoked when the animation has completed.
 */
inline fun <T : Animator> T.doOnceOnComplete(
    crossinline action: T.() -> Unit,
) {
    val ref = AtomicReference<Animator.AnimatorListener>()
    doOnComplete {
        action.invoke(this)
        ref.get()?.let(::removeListener)
    }.also(ref::set)
}
