package com.example.woopchat.recycler.listener

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.woopchat.recycler.DelegatingAdapter

class ScrollToEndListener(
    private val onScrollToEnd: () -> Unit,
    private val triggerCount: Int = 10
) : RecyclerView.OnScrollListener() {

    private var layoutManager: LinearLayoutManager? = null
    private var adapter: DelegatingAdapter? = null

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val layoutManager = (layoutManager ?: (recyclerView.layoutManager as LinearLayoutManager).also { layoutManager = it })
        val adapter = (adapter ?: (recyclerView.adapter as DelegatingAdapter).also { adapter = it })
        val itemCount = adapter.itemCount
        if (itemCount != 0 && layoutManager.findLastVisibleItemPosition() <= itemCount - triggerCount) {
            onScrollToEnd.invoke()
        }
    }
}
class ScrollToStartListener(
    private val onScrollToEnd: () -> Unit,
    private val triggerCount: Int = 10
) : RecyclerView.OnScrollListener() {

    private var layoutManager: LinearLayoutManager? = null
    private var adapter: DelegatingAdapter? = null

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val layoutManager = (layoutManager ?: (recyclerView.layoutManager as LinearLayoutManager).also { layoutManager = it })
        val adapter = (adapter ?: (recyclerView.adapter as DelegatingAdapter).also { adapter = it })
        val itemCount = adapter.itemCount
        Log.d("-------------", "${layoutManager.findFirstVisibleItemPosition()}")
        if (itemCount != 0 && layoutManager.findFirstVisibleItemPosition() <= triggerCount) {
            onScrollToEnd.invoke()
        }
    }
}