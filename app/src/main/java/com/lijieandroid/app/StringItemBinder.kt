package com.lijieandroid.app

import android.view.LayoutInflater
import android.view.ViewGroup
import com.lijieandroid.corelib.widget.ViewHolder
import kotlinx.android.synthetic.main.layout_string_item.view.*
import me.drakeet.multitype.ItemViewBinder

class StringItemBinder : ItemViewBinder<String, ViewHolder>() {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.layout_string_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, item: String) {
        with(holder.itemView) {
            textView.text = item
        }
    }
}