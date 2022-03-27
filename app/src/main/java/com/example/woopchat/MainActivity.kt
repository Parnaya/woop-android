package com.example.woopchat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.woopchat.databinding.ActivityMainBinding
import com.example.woopchat.service.WebsocketService
import com.example.woopchat.utils.setTextWithAnimation
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle


class MainActivity : AppCompatActivity() {

    lateinit var service: WebsocketService// = scarlet.create<SocketService>()

    lateinit var vimo: MainVimo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vimo = MainVimo(application.assets, AndroidLifecycle.ofApplicationForeground(application))
        vimo.onOpenedChat("/")
        ActivityMainBinding.inflate(layoutInflater).apply {
            val pagerAdapter = PagerAdapter(this@MainActivity)
            pager.currentItem = 2
            pager.adapter = pagerAdapter
            pager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    title.setTextWithAnimation(MainVimo.Screens[position].title, duration = 600)
                }
            })
        }.root.also(::setContentView)
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private inner class PagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = MainVimo.Screens.size
        override fun createFragment(position: Int): Fragment = ChatFragment()
    }
}