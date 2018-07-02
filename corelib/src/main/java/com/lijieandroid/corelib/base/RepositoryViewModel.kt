package com.lijieandroid.corelib.base

import android.arch.lifecycle.ViewModel

/**
 * 包含数据类的ViewModel
 */
open class RepositoryViewModel<out T : IRepository>(private val repository: T) : ViewModel() {

    fun getRepository(): T = repository

    /**
     * 被清除时，调用数据类的清除方法
     */
    override fun onCleared() {
        super.onCleared()
        repository.onCleared()
    }

}