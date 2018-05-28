package com.lijieandroid.corelib.os

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment

/**
 * Context启动一个activity
 */
inline fun <reified T : Activity> Context.start(bundle: Bundle? = null) {
    val intent = Intent(this, T::class.java)
    bundle?.let {
        intent.putExtras(it)
    }
    this.startActivity(intent)
}

/**
 * Context启动一个Activity For Result
 */
inline fun <reified T : Activity> Activity.start(requestCode: Int, bundle: Bundle? = null) {
    val intent = Intent(this, T::class.java)
    bundle?.let {
        intent.putExtras(it)
    }
    this.startActivityForResult(intent, requestCode)
}

/**
 * Fragment启动一个Activity
 */
inline fun <reified T : Activity> Fragment.start(bundle: Bundle? = null) {
    val intent = Intent(this.context, T::class.java)
    bundle?.let {
        intent.putExtras(it)
    }
    this.startActivity(intent)
}

/**
 * Fragment启动一个Activity For Result
 */
inline fun <reified T : Activity> Fragment.start(requestCode: Int, bundle: Bundle? = null) {
    val intent = Intent(this.context, T::class.java)
    bundle?.let {
        intent.putExtras(it)
    }
    this.startActivityForResult(intent, requestCode)
}