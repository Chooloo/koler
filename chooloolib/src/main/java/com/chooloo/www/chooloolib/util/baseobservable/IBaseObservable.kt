package com.chooloo.www.chooloolib.util.baseobservable

interface IBaseObservable<Listener> {
    fun registerListener(listener: Listener)
    fun unregisterListener(listener: Listener)
    fun invokeListeners(invoker: (Listener) -> Unit)
}