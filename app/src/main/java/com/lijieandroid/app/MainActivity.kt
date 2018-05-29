package com.lijieandroid.app

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerLayout.setLayoutManager(GridLayoutManager(this, 3))
        recyclerLayout.register(String::class.java, StringItemBinder())
        recyclerLayout.onRefresh = {
            val loadMore = loadMore(1)
            recyclerLayout.items = loadMore.toMutableList()
            recyclerLayout.finishRefresh()
        }
        recyclerLayout.onLoadMore = { _, page ->
            val loadMore = loadMore(page)
            recyclerLayout.items.addAll(loadMore)
            recyclerLayout.notifyItems()
            if (page == 5) {
                recyclerLayout.finishLoadMore(noMoreData = true)
            } else {
                recyclerLayout.finishLoadMore()
            }
        }
        val textView1 = LayoutInflater.from(this).inflate(R.layout.layout_string_item, null, false) as TextView
        textView1.text = "头部1"
        recyclerLayout.addHeader(textView1)

        val textView2 = LayoutInflater.from(this).inflate(R.layout.layout_string_item, null, false) as TextView
        textView2.text = "头部2"
        recyclerLayout.addHeader(textView2)


        val textView3 = LayoutInflater.from(this).inflate(R.layout.layout_string_item, null, false) as TextView
        textView3.text = "底部1"
        recyclerLayout.addFooter(textView3)

        val textView4 = LayoutInflater.from(this).inflate(R.layout.layout_string_item, null, false) as TextView
        textView4.text = "底部2"
        recyclerLayout.addFooter(textView4)

        val textView5 = LayoutInflater.from(this).inflate(R.layout.layout_string_item, null, false) as TextView
        textView5.text = "没有数据"
        recyclerLayout.emptyView = textView5

    }

    private fun loadMore(page: Int): List<String> {
        val list = arrayListOf<String>()
        (0..100).forEach {
            list.add("$page 页$it")
        }
        return list
    }


}
