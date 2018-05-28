package com.lijieandroid.corelib.base

import android.arch.lifecycle.Observer

/**
 * 添加状态的监听回调，包含获取数据失败和完成状态
 */
abstract class StatusObserver<T> : Observer<T> {

    open fun onStart() {}

    open fun onError(error: Throwable) {}

    open fun onComplete() {}

}