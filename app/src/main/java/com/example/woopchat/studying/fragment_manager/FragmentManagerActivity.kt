package com.example.woopchat.studying.fragment_manager

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.work.*
import com.example.woopchat.studying.work_manager.WorkerA
import com.example.woopchat.studying.work_manager.WorkerB
import com.example.woopchat.studying.work_manager.WorkerC
import com.example.woopchat.utils.MATCH_PARENT
import java.util.concurrent.TimeUnit

class FragmentManagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FrameLayout(this).apply {
            layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            background = ColorDrawable(Color.DKGRAY)
        }.also(::setContentView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
    }
}
