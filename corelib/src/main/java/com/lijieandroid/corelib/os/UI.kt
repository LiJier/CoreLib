package com.lijieandroid.corelib.os

import com.lijieandroid.corelib.App

fun getStatusBarHeight(): Int {
    var result = 0
    val resourceId = App.context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = App.context.resources.getDimensionPixelSize(resourceId)
    }
    return result
}