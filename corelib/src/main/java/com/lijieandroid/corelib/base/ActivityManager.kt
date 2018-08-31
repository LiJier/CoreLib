package com.lijieandroid.corelib.base

import android.app.Activity
import java.lang.ref.WeakReference

object ActivityManager {

    private val activitys = mutableListOf<WeakReference<Activity>>()

    fun add(activity: Activity) {
        val weakReference = WeakReference(activity)
        activitys.add(weakReference)
    }

    fun pop(activity: Activity) {
        var weakReference: WeakReference<Activity>? = null
        activitys.forEach {
            val get = it.get()
            if (get === activity) {
                weakReference = it
            }
        }
        weakReference?.let { activitys.remove(it) }
    }

    fun getCurrent(): Activity? {
        return activitys.last().get()
    }

    fun clear() {
        activitys.forEach { weakReference ->
            val get = weakReference.get()
            get?.let {
                if (it.isFinishing.not()) {
                    it.finish()
                }
            }
        }
        activitys.clear()
    }

}