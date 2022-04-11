package com.example.woopchat.live_data

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.launch

inline fun <State> LiveData<State>.scanObserve(
    lifecycleOwner: LifecycleOwner,
    crossinline onChanged: (prev: State?, curr: State) -> Unit
): Observer<State> {
    return object : Observer<State> {
        var prev: State? = null

        init {
            observe(lifecycleOwner, this)
        }

        override fun onChanged(state: State) {
            onChanged.invoke(prev, state)
            prev = state
        }
    }
}

fun <State> LiveData<State>.suspendScanObserve(
    lifecycleOwner: LifecycleOwner,
    onChanged: suspend (prev: State?, curr: State) -> Unit
): Observer<State> {
    val scope = lifecycleOwner.lifecycleScope
    val rendererChannel = scope.actor<State> {
        var prev: State? = null
        for (state in channel) {
            onChanged.invoke(prev, state)
            prev = state
        }
    }
    return object : Observer<State> {

        init {
            observe(lifecycleOwner, this)
        }

        override fun onChanged(state: State) {
            rendererChannel.trySend(state).onFailure {
                scope.launch {
                    rendererChannel.send(state)
                }
            }
        }
    }
}

fun <State> LiveData<State>.suspendObserve(
    lifecycleOwner: LifecycleOwner,
    onChanged: suspend (state: State) -> Unit
): Observer<State> {
    val scope = lifecycleOwner.lifecycleScope
    val rendererChannel = scope.actor<State> {
        for (state in channel) {
            onChanged.invoke(state)
        }
    }
    return object : Observer<State> {

        init {
            observe(lifecycleOwner, this)
        }

        override fun onChanged(state: State) {
            rendererChannel.trySend(state).onFailure {
                scope.launch {
                    rendererChannel.send(state)
                }
            }
        }
    }
}