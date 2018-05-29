package com.lijieandroid.corelib.widget

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.lijieandroid.corelib.R
import com.scwang.smartrefresh.layout.api.RefreshLayout
import kotlinx.android.synthetic.main.layout_recycler_layout.view.*
import me.drakeet.multitype.ItemViewBinder
import me.drakeet.multitype.MultiTypeAdapter

class RecyclerLayout : FrameLayout {

    val adapter: MultiTypeAdapter = MultiTypeAdapter()
    var emptyView: View? = null
        set(value) {
            field = value
            notifyItems()
        }
    var items = mutableListOf<Any>()
        set(value) {
            field = value
            notifyItems()
        }
    var onRefresh: ((RefreshLayout) -> Unit)? = null
    var onLoadMore: ((RefreshLayout, Int) -> Unit)? = null

    private val headerList = mutableListOf<View>()
    private val footerList = mutableListOf<View>()
    private val allItemList = mutableListOf<Any>()

    private var currentPageIndex: Int = 1

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        LayoutInflater.from(context).inflate(R.layout.layout_recycler_layout, this, true)
        adapter.register(View::class.java, HeaderFooterViewBinder(recycle_view))
        recycle_view.layoutManager = LinearLayoutManager(context)
        refresh_layout.setOnRefreshListener {
            onRefresh?.invoke(it)
        }
        refresh_layout.setOnLoadMoreListener {
            onLoadMore?.invoke(it, currentPageIndex + 1)
        }
    }

    fun <T> register(clazz: Class<out T>, binder: ItemViewBinder<T, *>) {
        adapter.register(clazz, binder)
    }

    fun setLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        recycle_view.layoutManager = layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanSizeLookup = layoutManager.spanSizeLookup
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val viewType = adapter.items[position]
                    return if (viewType is View) {
                        layoutManager.spanCount
                    } else {
                        spanSizeLookup?.getSpanSize(position) ?: 1
                    }
                }
            }
        }
    }

    fun autoRefresh() {
        refresh_layout.autoRefresh()
    }

    fun finishRefresh(success: Boolean = true) {
        refresh_layout.finishRefresh(success)
        if (success) {
            currentPageIndex = 1
            refresh_layout.setNoMoreData(false)
        }
    }

    fun finishLoadMore(success: Boolean = true, noMoreData: Boolean = false) {
        if (noMoreData) {
            refresh_layout.finishLoadMoreWithNoMoreData()
        } else {
            refresh_layout.finishLoadMore(success)
        }
        if (success) {
            currentPageIndex += 1
        }
    }

    fun addHeader(view: View) {
        headerList.add(view)
        notifyItems()
    }

    fun addFooter(view: View) {
        footerList.add(view)
        notifyItems()
    }

    fun notifyItems() {
        allItemList.clear()
        allItemList.addAll(headerList)
        if (items.isEmpty()) {
            emptyView?.let { allItemList.add(it) }
        }
        allItemList.addAll(items)
        allItemList.addAll(footerList)
        adapter.items = allItemList
        recycle_view.adapter?.notifyDataSetChanged() ?: run {
            recycle_view.adapter = adapter
        }
    }


}