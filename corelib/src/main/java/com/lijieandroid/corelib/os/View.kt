package com.lijieandroid.corelib.os

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.jakewharton.rxbinding2.view.RxView
import com.lijieandroid.corelib.rx.toMain
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


/**
 * 点击事件
 */
inline fun <T> View.onClickMap(crossinline next: (Any) -> T): Observable<T> {
    return RxView.clicks(this).throttleFirst(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread()).map { next.invoke(it) }
}

/**
 * 点击事件
 */
inline fun <T> View.onClick(crossinline next: (Any) -> T): Disposable {
    return RxView.clicks(this).throttleFirst(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread()).subscribe { next.invoke(it) }
}

/**
 * 长按事件
 */
inline fun <T> View.longClickMap(crossinline next: (Any) -> T): Observable<T> {
    return RxView.longClicks(this).throttleFirst(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread()).map { next.invoke(it) }
}

/**
 * 长按事件
 */
inline fun <T> View.longClick(crossinline next: (Any) -> T): Disposable {
    return RxView.longClicks(this).throttleFirst(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread()).subscribe { next.invoke(it) }
}

/**
 * gone
 */
fun View.gone() {
    this.visibility = View.GONE
}

/**
 * visible
 */
fun View.visible() {
    this.visibility = View.VISIBLE
}

/**
 * invisible
 */
fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun EditText.showInput(activity: Activity) {
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_FORCED)
}

fun EditText.hideInput(activity: Activity) {
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}