package com.example.woopchat.recycler

import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.woopchat.viewbinding.inflateBinding
import com.example.woopchat.recycler.diff.Diffable
import com.example.woopchat.recycler.holder.SimpleViewHolder

inline fun <reified T, reified H : RecyclerView.ViewHolder> TypedAdapter(
    crossinline createHolder: (parent: ViewGroup) -> H,
    crossinline bindHolder: H.(item: T, payloads: List<Any>) -> Unit = { _, _ -> },
    crossinline isSupported: (item: T) -> Boolean = { true },
): AdapterDelegate {
    return object : AdapterDelegate {

        override fun isSupported(items: List<Diffable>, position: Int): Boolean {
            val item = items[position]
            return item is T && isSupported.invoke(item)
        }

        override fun createHolder(parent: ViewGroup): H {
            return createHolder.invoke(parent)
        }

        override fun bindHolder(holder: RecyclerView.ViewHolder, items: List<Diffable>, position: Int, payloads: List<Any>) {
            bindHolder.invoke(holder as H, items[position] as T, payloads)
        }
    }
}

inline fun <reified T : Diffable, reified B : ViewBinding> BindingAdapter(
    crossinline setup: B.(parent: ViewGroup) -> Unit = {},
    crossinline bind: B.(item: T, position: Int, payloads: List<Any>) -> Unit = { _, _, _ -> },
    crossinline isSupported: (item: T) -> Boolean = { true },
    crossinline isStateful: () -> Boolean = { false },
    crossinline saveState: B.() -> Parcelable? = { null },
    crossinline restoreState: B.(Parcelable) -> Unit = {},
): AdapterDelegate {
    return object : AdapterDelegate {

        override val isStateful: Boolean
            get() = isStateful.invoke()

        override fun isSupported(items: List<Diffable>, position: Int): Boolean {
            val item = items[position]
            return item is T && isSupported.invoke(item)
        }

        override fun createHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val binding = inflateBinding<B>(parent)
            setup.invoke(binding, parent)
            return BindingHolder(binding)
        }

        override fun bindHolder(holder: RecyclerView.ViewHolder, items: List<Diffable>, position: Int, payloads: List<Any>) {
            bind.invoke(holder.binding(), items[position] as T, position, payloads)
        }

        override fun saveState(holder: RecyclerView.ViewHolder): Parcelable? {
            return saveState.invoke(holder.binding())
        }

        override fun restoreState(holder: RecyclerView.ViewHolder, state: Parcelable) {
            restoreState.invoke(holder.binding(), state)
        }

        @Suppress("UNCHECKED_CAST")
        private inline fun <reified B : ViewBinding> RecyclerView.ViewHolder.binding(): B {
            return (this as BindingHolder<B>).binding
        }
    }
}

inline fun <reified T: Diffable, reified V: View> ViewAdapter(
    crossinline create: (parent: ViewGroup) -> V,
    crossinline bind: V.(item: T, position: Int, payloads: List<Any>) -> Unit = { _, _, _ -> },
    crossinline isSupported: (item: T) -> Boolean = { true },
): AdapterDelegate {
    return object : AdapterDelegate {

        override fun isSupported(items: List<Diffable>, position: Int): Boolean {
            val item = items[position]
            return item is T && isSupported.invoke(item)
        }

        override fun createHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            return SimpleViewHolder(create.invoke(parent))
        }

        override fun bindHolder(holder: RecyclerView.ViewHolder, items: List<Diffable>, position: Int, payloads: List<Any>) {
            bind.invoke(holder.itemView as V, items[position] as T, position, payloads)
        }
    }
}

inline fun <reified T : Any> ViewBinding.onBound(block: (T) -> Unit) {
    root.getBound<T>()?.let(block)
}

@Suppress("UNCHECKED_CAST")
inline fun <reified B : ViewBinding> RecyclerView.ViewHolder.onBinding(block: B.() -> Unit) {
    block.invoke((this as BindingHolder<B>).binding)
}

class BindingHolder<B : ViewBinding>(
    val binding: B
) : RecyclerView.ViewHolder(binding.root)

inline fun <reified Item : Diffable, reified Binding : ViewBinding> GenericAdapter(
    crossinline setup: Binding.() -> Unit,
    crossinline bind: Binding.(Item) -> Unit,
    crossinline isSupported: (Item) -> Boolean = { true },
) = BindingAdapter<Item, Binding>(
    setup = { setup.invoke(this) },
    bind = { item, _, _ -> bind.invoke(this, item) },
    isSupported = isSupported,
)
