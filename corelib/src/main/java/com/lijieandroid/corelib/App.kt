package com.lijieandroid.corelib

import android.annotation.SuppressLint
import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication

open class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

}