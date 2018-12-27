package com.lijieandroid.corelib.widget

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.layout_recycler_layout.view.*
import me.drakeet.multitype.ItemViewBinder
import me.drakeet.multitype.MultiTypeAdapter
import me.drakeet.multitype.OneToManyFlow

class WrapRecyclerView : RecyclerView {

    private val adapter: MultiTypeAdapter = MultiTypeAdapter()
    var emptyView: View? = null
        set(value) {
            field = value
            notifyItems(isInit)
        }
    var items = mutableListOf<Any>()
        set(value) {
            field = value
            notifyItems()
        }

    private val headerList = mutableListOf<View>()
    private val footerList = mutableListOf<View>()
    private val allItemList = mutableListOf<Any>()
    private var isInit = true

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        adapter.register(View::class.java, HeaderFooterViewBinder(this))
        layoutManager = LinearLayoutManager(context)
        emptyView = defaultEmptyLayoutCreator?.invoke()
        isInit = false
    }

    override fun setLayoutManager(layoutManager: RecyclerView.LayoutManager) {
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
        super.setLayoutManager(layoutManager)
    }

    fun <T> register(clazz: Class<out T>, binder: ItemViewBinder<T, *>) {
        adapter.register(clazz, binder)
    }

    fun <T> register(clazz: Class<out T>): OneToManyFlow<T> {
        return adapter.register(clazz)
    }

    fun addHeader(view: View) {
        headerList.add(view)
        notifyItems()
    }

    fun addFooter(view: View) {
        footerList.add(view)
        notifyItems()
    }

    fun getHeaderCount(): Int = headerList.size

    fun getFooterCount(): Int = footerList.size

    private fun notifyItems(isInit: Boolean = false) {
        allItemList.clear()
        allItemList.addAll(headerList)
        if (isInit.not()) {
            if (items.isEmpty()) {
                emptyView?.let { allItemList.add(it) }
            }
        }
        allItemList.addAll(items)
        allItemList.addAll(footerList)
        adapter.items = allItemList
        getAdapter()?.notifyDataSetChanged() ?: run {
            setAdapter(adapter)
        }
    }

    companion object {
        var defaultEmptyLayoutCreator: (() -> View)? = null
    }


}