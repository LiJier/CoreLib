package com.lijieandroid.corelib.os

import android.annotation.SuppressLint
import android.content.SharedPreferences

/**
 * prefs.edit {
 *     putString("key", value)
 * }
 *
 * prefs.edit(commit = true) {
 *     putString("key", value)
 * }
 *
 */
@SuppressLint("ApplySharedPref")
inline fun SharedPreferences.edit(
        commit: Boolean = false,
        action: SharedPreferences.Editor.() -> Unit
) {
    val editor = edit()
    action(editor)
    if (commit) {
        editor.commit()
    } else {
        editor.apply()
    }
}