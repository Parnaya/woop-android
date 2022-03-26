package com.example.woopchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.woopchat.databinding.FragmentChatBinding

class ChatFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentChatBinding.inflate(inflater, container, false).apply {

        }.root
    }
}