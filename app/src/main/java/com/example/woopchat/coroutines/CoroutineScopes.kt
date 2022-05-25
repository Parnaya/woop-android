package com.example.woopchat.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob

interface CoroutineScopes {
    val io: CoroutineScope
    val main: CoroutineScope

    companion object : CoroutineScopes { //TODO what a hell? Why in use cases using that scope?
        override var io: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.io + ExceptionHandlers.io)
        override val main: CoroutineScope = MainScope()
    }
}
