package com.lijieandroid.corelib.http

interface HttpResult<T> {

    fun obtainIsSuccess(): Boolean
    fun obtainData(): T?
    fun obtainMessage(): String?

}