package com.lijieandroid.corelib.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo


/**
 * BaseActivity
 */
open class BaseActivity : AppCompatActivity() {

    /**
     * 耗时操作集合
     */
    private val compositeDisposable = CompositeDisposable()

    override fun onResume() {
        super.onResume()
        ActivityManager.add(this)
    }

    /**
     * 将耗时操作加入集合
     */
    fun Disposable.addToCompositeDisposable() {
        this.addTo(compositeDisposable)
    }

    override fun onBackPressed() {
        var back = true
        supportFragmentManager.fragments.forEach {
            if (it is BaseFragment) {
                if (it.onBackPressed()) {
                    back = false
                    return@forEach
                }
            }
        }
        if (back) {
            super.onBackPressed()
        }
    }

    override fun onStop() {
        super.onStop()
        ActivityManager.pop(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        //界面销毁时取消所有耗时操作
        if (compositeDisposable.isDisposed.not()) {
            compositeDisposable.dispose()
        }
    }

}