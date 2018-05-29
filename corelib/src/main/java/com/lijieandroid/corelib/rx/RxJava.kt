package com.lijieandroid.corelib.rx

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * 在指定线程和Android Main线程之间切换
 */
fun <T> Observable<T>.toMain(scheduler: Scheduler): Observable<T> {
    return this.subscribeOn(scheduler).observeOn(AndroidSchedulers.mainThread())
}

