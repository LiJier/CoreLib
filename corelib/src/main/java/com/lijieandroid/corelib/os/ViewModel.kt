package com.lijieandroid.corelib.os

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.lijieandroid.corelib.base.IRepository


/**
 * 获取普通ViewModel
 */
inline fun <reified T : ViewModel> FragmentActivity.getViewModel(): T {
    return ViewModelProviders.of(this).get(T::class.java)
}

/**
 * 获取数据类ViewModel
 */
inline fun <reified T : ViewModel, reified R : IRepository> FragmentActivity.getViewModel(r: R): T {
    return ViewModelProviders.of(this, RepositoryModelFactory(R::class.java, r)).get(T::class.java)
}

/**
 * 获取普通ViewModel
 */
inline fun <reified T : ViewModel> Fragment.getViewModel(): T {
    return ViewModelProviders.of(this).get(T::class.java)
}

/**
 * 获取数据类ViewModel
 */
inline fun <reified T : ViewModel, reified R : IRepository> Fragment.getViewModel(r: R): T {
    return ViewModelProviders.of(this, RepositoryModelFactory(R::class.java, r)).get(T::class.java)
}

/**
 * 数据类ViewModel的Factory
 */
class RepositoryModelFactory<R>(private val parameterTypes: Class<*>, val r: R) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        try {
            return modelClass.getConstructor(parameterTypes).newInstance(r)
        } catch (e: InstantiationException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        }
    }

}