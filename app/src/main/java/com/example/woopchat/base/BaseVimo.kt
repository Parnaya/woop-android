package com.example.woopchat.base

import androidx.lifecycle.ViewModel
import com.example.woopchat.coroutines.CoroutineScopeOwner
import com.example.woopchat.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

open class BaseVimo : ViewModel(), CoroutineScopeOwner {
    override val coroutineScope = CoroutineScope(
        SupervisorJob() +
        Dispatchers.bg
    )

    override fun onCleared() {

        super.onCleared()
        coroutineScope.cancel()
    }
}