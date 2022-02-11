package com.chooloo.www.chooloolib.util

import androidx.lifecycle.MutableLiveData

class DataLiveEvent<T> : MutableLiveData<Event<T?>> {
    constructor() : super()
    constructor(value: Event<T>) : super(value)

    fun call(value: T) {
        this@DataLiveEvent.value = Event(value)
    }
}

class LiveEvent : MutableLiveData<Event<Int>>() {
    fun call() {
        value = Event(0)
    }
}