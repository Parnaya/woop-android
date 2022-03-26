package com.example.woopchat.utils

import android.animation.ObjectAnimator
import android.view.View
import android.widget.TextView
import com.example.woopchat.R
import java.util.concurrent.TimeUnit

private val TextView.textAnimator: ObjectAnimator
    get() = getTag(R.id.tag_text_animator) as? ObjectAnimator ?: ObjectAnimator
        .ofFloat(this, View.ALPHA, 1f)
        .apply {
            interpolator = Easing.standard
            setTag(R.id.tag_text_animator, this)
        }

fun TextView.setTextWithAnimation(
    string: String,
    animate: Boolean = true,
    duration: Long = 300,
    unit: TimeUnit = TimeUnit.MILLISECONDS,
) {
    val halfDuration = unit.toMillis(duration) / 2
    val animateOnlyFadeIn = text.isBlank() || string == text
    when {
        !animate -> textAnimator.apply {
            textAnimator.cancel()
            alpha = 1f
            text = string
        }
        animateOnlyFadeIn -> textAnimator.apply {
            removeAllListeners()
            if (text.isBlank()) {
                alpha = 0f
                text = string
            }
            this.duration = (halfDuration * (1 - alpha)).toLong()
            setFloatValues(alpha, 1f)
            start()
        }
        else -> textAnimator.apply {
            removeAllListeners()
            doOnceOnComplete {
                text = string
                this.duration = halfDuration
                setFloatValues(0f, 1f)
                start()
            }
            this.duration = (halfDuration * alpha).toLong()
            setFloatValues(alpha, 0f)
            start()
        }
    }
}