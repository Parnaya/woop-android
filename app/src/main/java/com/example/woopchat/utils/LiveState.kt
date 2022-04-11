package com.example.woopchat.utils

import android.os.Looper
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.example.woopchat.live_data.suspendScanObserve
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

interface LiveState<T> {
    val state: T?
    fun setState(state: T)
    fun observeState(owner: LifecycleOwner, onUpdate: suspend (T?, T) -> Unit)

    companion object {
        operator fun <T : Any> invoke(): LiveState<T> = LiveStateImpl()
    }
}

@MainThread
inline fun <reified T> LiveState<T>.mutateState(mutator: T.() -> T) {
    val oldState = state ?: return
    val newState = mutator.invoke(oldState)
    setState(newState)
}

private class LiveStateImpl<T : Any> : LiveState<T> {

    private val liveData = MutableLiveData<T>()

    override val state: T?
        get() = liveData.value

    override fun setState(state: T) {
        if (isMainThread()) {
            liveData.value = state
        } else {
            MainScope().launch {
                setState(state)
            }
        }
    }

    override fun observeState(owner: LifecycleOwner, onUpdate: suspend (T?, T) -> Unit) {
        liveData.suspendScanObserve(owner, onUpdate)
    }
}

fun isMainThread(): Boolean {
    return Looper.getMainLooper() == Looper.myLooper()
}