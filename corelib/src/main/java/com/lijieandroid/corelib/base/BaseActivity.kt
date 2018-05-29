package com.lijieandroid.corelib.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo


/**
 * BaseActivity
 */
abstract class BaseActivity : AppCompatActivity() {

    /**
     * 布局文件
     */
    abstract val layoutRes: Int
    /**
     * 耗时操作集合
     */
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (layoutRes != 0) {
            setContentView(layoutRes)
        }
    }

    /**
     * 将耗时操作加入集合
     */
    fun Disposable.addToCompositeDisposable() {
        this.addTo(compositeDisposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        //界面销毁时取消所有耗时操作
        if (compositeDisposable.isDisposed.not()) {
            compositeDisposable.dispose()
        }
    }

}