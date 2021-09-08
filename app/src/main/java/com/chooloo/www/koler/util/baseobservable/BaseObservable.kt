package com.chooloo.www.koler.util.baseobservable

import java.util.*
import kotlin.collections.HashSet

open class BaseObservable<Listener> : IBaseObservable<Listener> {
    private val _listeners by lazy { HashSet<Listener>() }

    val listeners: Set<Listener>
        @Synchronized
        get() = Collections.unmodifiableSet(_listeners)

    @Synchronized
    override fun registerListener(listener: Listener) {
        _listeners.remove(listener)
        _listeners.add(listener)
    }

    @Synchronized
    override fun unregisterListener(listener: Listener) {
        _listeners.remove(listener)
    }

    override fun invokeListeners(invoker: (Listener) -> Unit) {
        listeners.forEach(invoker::invoke)

    }

    protected open fun onActive() {}
    protected open fun onInActive() {}
}