package com.lijieandroid.corelib.http

interface HttpResult<out T> {

    fun obtainIsSuccess(): Boolean
    fun obtainData(): T?
    fun obtainMessage(): String?

}