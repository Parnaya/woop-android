package com.example.woopchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.woopchat.base.assistedVimo
import com.example.woopchat.databinding.FragmentChatBinding
import com.example.woopchat.databinding.ItemLeftMessageBinding
import com.example.woopchat.databinding.ItemRightMessageBinding
import com.example.woopchat.recycler.*
import com.example.woopchat.recycler.diff.Diffable
import com.example.woopchat.recycler.listener.ScrollToStartListener
import javax.inject.Inject


class ChatFragment : Fragment() {

    @Inject
    lateinit var fac: ChatVimo.Factory

    val vimo: ChatVimo by assistedVimo {
        fac.create(
            it?.getString(UserTag)!!,
            it.getString(ChatTag)!!,
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        WoopApp.ChatComponent.inject(this)

        return FragmentChatBinding.inflate(inflater, container, false).apply {
            list.adapter = AdapterWithVerticalDividers(
                requireContext(),
                LeftMessageAdapter(),
                RightMessageAdapter(),
            )
            list.setHasFixedSize(false)
            list.disableChangeAnimations()
            val linearLayoutManager = LinearLayoutManager(requireContext())
            linearLayoutManager.stackFromEnd = true
            list.layoutManager = linearLayoutManager
            button.setOnClickListener {
                val inText = input.text.toString()
                if (inText.isNotBlank()) {
                    input.text.clear()
                    val diffs = vimo.sendMessage(inText)
                    list.addItems(diffs)
                    list.smoothScrollToPosition(list.getDelegatingAdapter().itemCount - 1)
                }
            }
            val scrollToEndListener = ScrollToStartListener(vimo::onScrollToEnd)
            list.addOnScrollListener(scrollToEndListener) //TODO send only if there is no answer on prev
            vimo.observeState(viewLifecycleOwner) { _, curr ->
                list.setItems(curr.items)
            }
        }.root
    }

    fun RecyclerView.disableChangeAnimations() {
        (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
    }

    companion object {
        private const val ChatTag = "ChatTag"
        private const val UserTag = "UserTag"

        operator fun invoke(user: String, chat: String) = ChatFragment().apply {
            arguments = bundleOf(
                ChatTag to chat,
                UserTag to user,
            )
        }
    }
}

data class LeftMessage(
    val text: String,
    override val id: String,
) : Diffable

data class RightMessage(
    val text: String,
    override val id: String,
) : Diffable

inline fun LeftMessageAdapter() = BindingAdapter<LeftMessage, ItemLeftMessageBinding>(
    bind = { item, _, _ ->
        text.text = item.text
    },
)

inline fun RightMessageAdapter() = BindingAdapter<RightMessage, ItemRightMessageBinding>(
    bind = { item, _, _ ->
        text.text = item.text
    },
)