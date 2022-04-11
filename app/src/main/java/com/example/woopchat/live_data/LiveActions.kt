package com.example.woopchat.live_data

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.example.woopchat.utils.isMainThread
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

interface LiveActions<T> {
    fun addAction(element: T)
    fun observeActions(owner: LifecycleOwner, onAction: suspend (T) -> Unit)

    companion object {
        operator fun <T> invoke(): LiveActions<T> = LiveActionsImpl()
    }
}

private class LiveActionsImpl<T> : LiveActions<T> {

    private val liveData = MutableLiveData<T?>()
    private val buffer = ArrayDeque<T>()

    override fun addAction(element: T) {
        if (isMainThread()) {
            if (liveData.value == null) {
                liveData.value = element
            } else {
                buffer.add(element)
            }
        } else {
            MainScope().launch {
                addAction(element)
            }
        }
    }

    override fun observeActions(owner: LifecycleOwner, onAction: suspend (T) -> Unit) {
        liveData.suspendObserve(owner) { action ->
            if (action != null) {
                onAction.invoke(action)
                liveData.value = buffer.removeFirstOrNull()
            }
        }
    }
}