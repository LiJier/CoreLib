package com.lijieandroid.corelib.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lijieandroid.corelib.rx.toMain
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * BaseFragment
 */
abstract class BaseFragment : Fragment() {

    /**
     * 布局文件
     */
    abstract val layoutRes: Int
    /**
     * 耗时操作集合
     */
    private val compositeDisposable = CompositeDisposable()

    /**
     * 是否已初始化数据
     */
    private var hasInitData = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (layoutRes != 0) {
            return inflater.inflate(layoutRes, container, false)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun Disposable.addToCompositeDisposable() {
        this.addTo(compositeDisposable)
    }

    fun initData() {
        if (hasInitData.not()) {
            Observable.timer(500, TimeUnit.MILLISECONDS)
                    .toMain(Schedulers.newThread())
                    .subscribe { onInitData() }
                    .addToCompositeDisposable()
            hasInitData = true
        }
    }

    protected open fun onInitData() {}

    override fun onDestroy() {
        super.onDestroy()
        //销毁时取消所有耗时操作
        if (compositeDisposable.isDisposed.not()) {
            compositeDisposable.dispose()
        }
    }

}