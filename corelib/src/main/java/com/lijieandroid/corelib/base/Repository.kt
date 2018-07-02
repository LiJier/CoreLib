package com.lijieandroid.corelib.base

import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo

/**
 * 数据类
 */
open class Repository : IRepository {

    /**
     * 耗时操作集合
     */
    private val compositeDisposable = CompositeDisposable()

    open inner class RxObserver<T>(private val statusLiveData: StatusLiveData<T>? = null) : Observer<T> {

        override fun onComplete() {
            statusLiveData?.onComplete()
        }

        override fun onSubscribe(d: Disposable) {
            d.addToCompositeDisposable()
            statusLiveData?.onStart()
        }

        override fun onNext(t: T) {
            statusLiveData?.postValue(t)
        }

        override fun onError(e: Throwable) {
            e.printStackTrace()
            statusLiveData?.onError(e)
            onComplete()
        }
    }

    /**
     * 将耗时操作加入集合
     */
    fun Disposable.addToCompositeDisposable() {
        this.addTo(compositeDisposable)
    }

    /**
     * 清除，在ViewModel中使用时，会在ViewModel清除时调用
     */
    override fun onCleared() {
        //清除时，取消所有耗时操作
        if (compositeDisposable.isDisposed.not()) {
            compositeDisposable.dispose()
        }
    }
}