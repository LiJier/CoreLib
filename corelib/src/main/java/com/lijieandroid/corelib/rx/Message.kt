package com.lijieandroid.corelib.rx

data class Message<T>(val code: Int, val message: T) {

    class Empty

}