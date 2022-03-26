package com.example.woopchat.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

interface CoroutineScopeOwner : CoroutineScope {
    val coroutineScope: CoroutineScope

    override val coroutineContext: CoroutineContext
        get() = coroutineScope.coroutineContext
}