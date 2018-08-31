package com.lijieandroid.corelib.widget

import android.content.Context
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RadioButton
import com.lijieandroid.corelib.R
import com.lijieandroid.corelib.number.dpToPx
import com.lijieandroid.corelib.rx.toMain
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_indicators_pager.view.*
import java.util.concurrent.TimeUnit


class IndicatorsViewPager : FrameLayout {

    private var loop = false
    private var indicators: Int = 0
    private var autoScroll = false
    private val onPageChangeListeners: MutableList<ViewPager.OnPageChangeListener> = arrayListOf()
    private var disposable: Disposable? = null
    var scrollTime = 2

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorsViewPager)
        loop = typedArray.getBoolean(R.styleable.IndicatorsViewPager_loop, false)
        indicators = typedArray.getResourceId(R.styleable.IndicatorsViewPager_indicators, 0)
        autoScroll = typedArray.getBoolean(R.styleable.IndicatorsViewPager_auto_scroll, false)
        typedArray.recycle()
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        LayoutInflater.from(context).inflate(R.layout.layout_indicators_pager, this, true)
    }

    fun setAdapter(adapter: PagerAdapter) {
        if (loop) {
            view_pager.adapter = LoopAdapter(adapter)
        } else {
            view_pager.adapter = adapter
        }
        if (autoScroll) {
            startAutoScroll()
        }
        setPagerIndicators(adapter)
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

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

        })
        addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                for (i in 0 until radio_group.childCount) {
                    val radioButton = radio_group.getChildAt(i) as RadioButton
                    radioButton.isChecked = false
                }
                val radioButton = radio_group.getChildAt(position) as RadioButton
                radioButton.isChecked = true
            }

        })
    }

    fun addOnPageChangeListener(listener: ViewPager.OnPageChangeListener) {
        onPageChangeListeners.add(listener)
    }

    private fun setPagerIndicators(adapter: PagerAdapter) {
        radio_group.removeAllViews()
        (0 until adapter.count).forEach {
            val radioButton = RadioButton(context)
            if (indicators != 0) {
                radioButton.buttonDrawable = ResourcesCompat.getDrawable(resources, indicators, null)
            }
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
            val margin = 8f.dpToPx().toInt()
            layoutParams.setMargins(margin, margin, margin, margin)
            radioButton.layoutParams = layoutParams
            radioButton.isChecked = it == 0
            radio_group.addView(radioButton)
        }
    }

    private fun startAutoScroll() {
        disposable?.let {
            if (it.isDisposed.not()) {
                it.dispose()
            }
        }
        disposable = Observable.interval(scrollTime.toLong(), TimeUnit.SECONDS)
                .toMain(Schedulers.newThread())
                .subscribe { _ ->
                    view_pager.adapter?.let {
                        if (view_pager.currentItem == it.count - 1) {
                            view_pager.currentItem = 0
                        } else {
                            view_pager.currentItem += 1
                        }
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

        override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
            adapter.destroyItem(container, position % adapter.count, any)
        }

    }

}