package com.lijieandroid.corelib.os

import android.content.Context
import android.support.annotation.StringRes
import android.widget.Toast

/**
 * 显示一个[Toast]
 *
 * @param duration 显示时长, 默认 [Toast.LENGTH_SHORT]
 */
fun Context.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT): Toast {
    return Toast.makeText(this, text, duration).apply { show() }
}

/**
 * 显示一个[Toast]
 *
 * @param resId 字符串的Resource id
 * @param duration 显示时长, 默认 [Toast.LENGTH_SHORT]
 */
fun Context.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT): Toast {
    return Toast.makeText(this, resId, duration).apply { show() }
}