package com.lijieandroid.corelib.base

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer

/**
 * 添加状态的LiveData，包含获取数据失败和完成状态
 */
class StatusLiveData<T> : MutableLiveData<T>() {

    private var statusObserver: StatusObserver<T>? = null

    override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
        super.observe(owner, observer)
        if (observer is StatusObserver) {
            statusObserver = observer
        }
    }

    fun onStart() {
        statusObserver?.onStart()
    }

    fun onError(error: Throwable) {
        statusObserver?.onError(error)
    }

    fun onComplete() {
        statusObserver?.onComplete()
    }

}