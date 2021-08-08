package com.chooloo.www.koler.util

import java.util.*
import kotlin.collections.HashSet

open class BaseObservable<Listener> {
    private val _listeners by lazy { HashSet<Listener>() }

    val listeners: Set<Listener>
        get() = Collections.unmodifiableSet(_listeners)

    @Synchronized
    fun registerListener(listener: Listener) {
        _listeners.remove(listener)
        _listeners.add(listener)
    }

    @Synchronized
    fun unregisterListener(listener: Listener) {
        _listeners.remove(listener)
    }

    protected fun invokeListeners(invoker: (Listener) -> Unit) {
        _listeners.forEach(invoker::invoke)
    }
}