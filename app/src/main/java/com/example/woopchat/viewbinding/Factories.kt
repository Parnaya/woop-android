package com.example.woopchat.viewbinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.woopchat.utils.inflater

val BindingInflaters = mutableMapOf<Class<*>, (ViewGroup) -> ViewBinding>()
val ViewBinders = mutableMapOf<Class<*>, (View) -> ViewBinding>()

@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewBinding> BindingInflater(): (ViewGroup) -> T {
    return BindingInflaters.getOrPut(T::class.java) {
        val inflate = T::class.java.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
        return@getOrPut { parent: ViewGroup ->
            inflate.invoke(null, parent.inflater, parent, false) as T
        }
    } as (ViewGroup) -> T
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewBinding> ViewBinder(): (View) -> T {
    return ViewBinders.getOrPut(T::class.java) {
        val inflate = T::class.java.getMethod("bind", View::class.java)
        return@getOrPut { view: View ->
            inflate.invoke(null, view) as T
        }
    } as (View) -> T
}

inline fun <reified T : ViewBinding> inflateBinding(parent: ViewGroup): T {
    return BindingInflater<T>().invoke(parent)
}
