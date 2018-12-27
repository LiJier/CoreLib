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
import android.widget.ImageView
import android.widget.LinearLayout
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
    private var indicatorsSelected = 0
    private var indicatorsUnselected = 0
    private var autoScroll = false
    private val onPageChangeListeners: MutableList<ViewPager.OnPageChangeListener> = arrayListOf()
    private var disposable: Disposable? = null
    private var indicatorsMargin = 0
    var scrollTime = 5

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorsViewPager)
        loop = typedArray.getBoolean(R.styleable.IndicatorsViewPager_loop, false)
        indicatorsSelected = typedArray.getResourceId(R.styleable.IndicatorsViewPager_indicators_selected, 0)
        indicatorsUnselected = typedArray.getResourceId(R.styleable.IndicatorsViewPager_indicators_unselected, 0)
        autoScroll = typedArray.getBoolean(R.styleable.IndicatorsViewPager_auto_scroll, false)
        scrollTime = typedArray.getInteger(R.styleable.IndicatorsViewPager_scroll_time, 5)
        indicatorsMargin = typedArray.getDimensionPixelSize(R.styleable.IndicatorsViewPager_indicators_margin, 8f.dpToPx().toInt())
        typedArray.recycle()
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        LayoutInflater.from(context).inflate(R.layout.layout_indicators_pager, this, true)
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
                onPageChangeListeners.forEach {
                    it.onPageScrollStateChanged(state)
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                onPageChangeListeners.forEach {
                    view_pager.adapter?.let { adapter ->
                        it.onPageScrolled(position % adapter.count, positionOffset, positionOffsetPixels)
                    }
                }
            }

            override fun onPageSelected(position: Int) {
                onPageChangeListeners.forEach {
                    view_pager.adapter?.let { adapter ->
                        it.onPageSelected(position % adapter.count)
                    }
                }
            }

        })
        addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                for (i in 0 until indicators_layout.childCount) {
                    val imageView = indicators_layout.getChildAt(i) as? ImageView
                    imageView?.setImageDrawable(ResourcesCompat.getDrawable(resources, indicatorsUnselected, null))
                }
                val imageView = indicators_layout.getChildAt(position) as? ImageView
                imageView?.setImageDrawable(ResourcesCompat.getDrawable(resources, indicatorsSelected, null))
            }

        })
    }

    fun setAdapter(adapter: PagerAdapter) {
        if (loop) {
            view_pager.adapter = LoopAdapter(adapter)
        } else {
            view_pager.adapter = adapter
        }
        setPagerIndicators(adapter)
        if (autoScroll) {
            startAutoScroll()
        }
    }

    fun addOnPageChangeListener(listener: ViewPager.OnPageChangeListener) {
        onPageChangeListeners.add(listener)
    }

    private fun setPagerIndicators(adapter: PagerAdapter) {
        indicators_layout.removeAllViews()
        (0 until adapter.count).forEach {
            val imageView = ImageView(context)
            imageView.setImageDrawable(ResourcesCompat.getDrawable(resources, indicatorsUnselected, null))
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(indicatorsMargin, indicatorsMargin, indicatorsMargin, indicatorsMargin)
            imageView.layoutParams = layoutParams
            if (it == 0) {
                imageView.setImageDrawable(ResourcesCompat.getDrawable(resources, indicatorsSelected, null))
            }
            indicators_layout.addView(imageView)
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
                .subscribe {
                    view_pager.adapter?.let { pagerAdapter ->
                        if (view_pager.currentItem == pagerAdapter.count - 1) {
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