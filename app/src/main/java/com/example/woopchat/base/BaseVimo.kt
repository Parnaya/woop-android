package com.example.woopchat.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import com.example.woopchat.WoopApp
import com.example.woopchat.coroutines.CoroutineScopeOwner
import com.example.woopchat.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineExceptionHandler
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

//inline fun <reified T : BaseVimo> AppCompatActivity.vimo(): Lazy<T> {
//    return viewModels(WoopApp.AppComponent::getVimoFactory)
//}

inline fun <reified T : ViewModel> Fragment.assistedVimo(
    crossinline creator: (Bundle?) -> T
): Lazy<T> {
    return createViewModelLazy(
        T::class,
        storeProducer = { viewModelStore },
        factoryProducer = { createAbstractViewModelFactory(creator) },
    )
}

inline fun <reified E : ViewModel> Fragment.createAbstractViewModelFactory(
    crossinline creator: (Bundle?) -> E
): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return creator(arguments) as T
    }
}

@MainThread
public inline fun <reified VM : ViewModel> Fragment.viewModels(
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> = createViewModelLazy(
    VM::class,
    { ownerProducer().viewModelStore },
    factoryProducer,
)