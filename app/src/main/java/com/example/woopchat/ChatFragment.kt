package com.example.woopchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.woopchat.databinding.FragmentChatBinding
import com.example.woopchat.databinding.ItemLeftMessageBinding
import com.example.woopchat.databinding.ItemRightMessageBinding
import com.example.woopchat.recycler.*
import com.example.woopchat.recycler.diff.Diffable
import com.example.woopchat.recycler.listener.ScrollToEndListener
import com.example.woopchat.recycler.listener.ScrollToStartListener


class ChatFragment : Fragment() {

    lateinit var vimo: ChatVimo // TODO inject vimo

    private val userTag by lazy { arguments?.getString(UserTag)!! }
    private val chatTag by lazy { arguments?.getString(ChatTag)!! }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mainVimo = (activity as MainActivity).vimo
        vimo = ChatVimo(
            userTag = userTag,
            chatTag = chatTag,
            socketUseCases = mainVimo.useCases,
        )
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