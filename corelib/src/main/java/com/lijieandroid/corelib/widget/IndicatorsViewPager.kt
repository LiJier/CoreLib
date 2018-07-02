package com.lijieandroid.corelib.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import com.lijieandroid.corelib.R
import com.lijieandroid.corelib.number.dpToPx

class IndicatorsViewPager : ViewPager {

    private var loop = false
    private lateinit var indicators: Drawable
    private var autoScroll = false
    private lateinit var indicatorsLayout: RadioGroup
    private lateinit var pageChangeListener: PageChangeListener
    private val onPageChangeListeners: MutableList<OnPageChangeListener> = arrayListOf()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorsViewPager)
        loop = typedArray.getBoolean(R.styleable.IndicatorsViewPager_loop, false)
        indicators = typedArray.getDrawable(R.styleable.IndicatorsViewPager_indicators)
        autoScroll = typedArray.getBoolean(R.styleable.IndicatorsViewPager_auto_scroll, false)
        typedArray.recycle()
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        indicatorsLayout = RadioGroup(context)
        val padding = 8f.dpToPx().toInt()
        indicatorsLayout.setPadding(padding, padding, padding, padding)
        val layoutParams = ViewPager.LayoutParams()
        layoutParams.isDecor = true
        layoutParams.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        addView(indicatorsLayout)
    }

    override fun setAdapter(adapter: PagerAdapter?) {
        if (loop) {
            super.setAdapter(if (adapter == null) adapter else LoopAdapter(adapter))
        } else {
            super.setAdapter(adapter)
        }
        adapter?.let {
            pageChangeListener = PageChangeListener(it)
            super.addOnPageChangeListener(pageChangeListener)
        } ?: run {
            if (::pageChangeListener.isInitialized) {
                super.removeOnPageChangeListener(pageChangeListener)
            }
        }
    }

    override fun addOnPageChangeListener(listener: OnPageChangeListener) {
        onPageChangeListeners.add(listener)
    }

    private fun setPagerIndicators(adapter: PagerAdapter) {
        indicatorsLayout.removeAllViews()
        (0 until adapter.count).forEach {
            val radioButton = RadioButton(context)
            indicatorsLayout.addView(radioButton)
            val padding = 8f.dpToPx().toInt()
            radioButton.setPadding(padding, 0, padding, 0)
            radioButton.isChecked = it == 0
        }
        addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                val radioButton = indicatorsLayout.getChildAt(position % adapter.count) as RadioButton
                radioButton.isChecked = true
            }

        })
    }

    private fun startAutoScroll() {

    }

    inner class PageChangeListener(private val adapter: PagerAdapter) : OnPageChangeListener {

        override fun onPageScrollStateChanged(state: Int) {
            onPageChangeListeners.forEach {
                it.onPageScrollStateChanged(state)
            }
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            onPageChangeListeners.forEach {
                it.onPageScrolled(position % adapter.count, positionOffset, positionOffsetPixels)
            }
        }

        override fun onPageSelected(position: Int) {
            onPageChangeListeners.forEach {
                it.onPageSelected(position % adapter.count)
            }
        }

    }

    inner class LoopAdapter(private val adapter: PagerAdapter) : PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            return adapter.instantiateItem(container, position % adapter.count)
        }

        override fun isViewFromObject(view: View, any: Any): Boolean {
            return adapter.isViewFromObject(view, any)
        }

        override fun getCount(): Int {
            return Int.MAX_VALUE
        }

    }

}