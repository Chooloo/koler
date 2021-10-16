package com.chooloo.www.koler.util.baseobservable

interface IBaseObservable<Listener> {
    fun registerListener(listener: Listener)
    fun unregisterListener(listener: Listener)
    fun invokeListeners(invoker: (Listener) -> Unit)
}