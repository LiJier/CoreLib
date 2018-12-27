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
import com.lijieandroid.corelib.widget.WrapRecyclerView.Companion.defaultEmptyLayoutCreator
import com.scwang.smartrefresh.layout.api.RefreshLayout
import kotlinx.android.synthetic.main.layout_recycler_layout.view.*
import me.drakeet.multitype.ItemViewBinder
import me.drakeet.multitype.MultiTypeAdapter
import me.drakeet.multitype.OneToManyFlow

class RecyclerLayout : FrameLayout {

    var emptyView: View? = null
        set(value) {
            field = value
            recycle_view.emptyView = value
        }
    var items = mutableListOf<Any>()
        set(value) {
            field = value
            recycle_view.items = value
        }
    var onRefresh: ((RefreshLayout) -> Unit)? = null
    var onLoadMore: ((RefreshLayout, Int) -> Unit)? = null

    private var currentPageIndex: Int = 1

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        LayoutInflater.from(context).inflate(R.layout.layout_recycler_layout, this, true)
        refresh_layout.setOnRefreshListener {
            onRefresh?.invoke(it)
        }
        refresh_layout.setOnLoadMoreListener {
            onLoadMore?.invoke(it, currentPageIndex + 1)
        }
    }

    fun setLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        recycle_view.layoutManager = layoutManager
    }

    fun <T> register(clazz: Class<out T>, binder: ItemViewBinder<T, *>) {
        recycle_view.register(clazz, binder)
    }

    fun <T> register(clazz: Class<out T>): OneToManyFlow<T> {
        return recycle_view.register(clazz)
    }

    fun addHeader(view: View) {
        recycle_view.addHeader(view)
    }

    fun addFooter(view: View) {
        recycle_view.addFooter(view)
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

    fun getHeaderCount(): Int = recycle_view.getHeaderCount()

    fun getFooterCount(): Int = recycle_view.getFooterCount()

    fun getRecycleView() = recycle_view
    fun getRefreshLayout() = refresh_layout

}