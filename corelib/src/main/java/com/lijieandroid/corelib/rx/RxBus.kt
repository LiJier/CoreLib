package com.lijieandroid.corelib.rx

import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable

/**
 * RxBus.post(Message(0, Message.Empty()))
 * RxBus.toObservable<Message.Empty>(0).subscribe {...}
 *
 */
object RxBus {

    private val bus: Relay<Message<Any>> = PublishRelay.create<Message<Any>>().toSerialized()

    fun post(message: Message<Any>) {
        bus.accept(message)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> toObservable(code: Int): Observable<T> {
        return bus.filter { it.code == code }.map { it.message as T }
    }

}