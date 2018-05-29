package com.lijieandroid.corelib.number

import android.support.v4.content.res.ResourcesCompat
import android.text.format.DateFormat
import android.util.TypedValue
import com.lijieandroid.corelib.App
import com.lijieandroid.corelib.App.Companion.context

fun Int.toColor(): Int = ResourcesCompat.getColor(App.context.resources, this, null)

fun Float.dpToPx(): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)

fun Float.spToPx(): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, context.resources.displayMetrics)

fun Long.toTimeString(format: String = "yyyy-MM-dd HH:mm:ss"): CharSequence = DateFormat.format(format, this)