package com.lijieandroid.corelib.http

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import java.io.IOException

/**
 * 处理接口返回实体类，获取data
 */
object HttpResultHandle {

    fun <T> handle(): ObservableTransformer<HttpResult<T>, T> {
        return ObservableTransformer { upstream ->
            upstream.flatMap {
                if (it.obtainIsSuccess()) {
                    return@flatMap Observable.just(it.obtainData())
                } else {
                    return@flatMap Observable.error<T>(IOException(it.obtainMessage() ?: "know "))
                }
            }
        }
    }

}