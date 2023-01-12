package com.chooloo.www.chooloolib.ui.base

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.chooloo.www.chooloolib.util.DataLiveEvent
import com.chooloo.www.chooloolib.util.LiveEvent
import com.chooloo.www.chooloolib.util.MutableDataLiveEvent
import com.chooloo.www.chooloolib.util.MutableLiveEvent

open class BaseViewState : ViewModel() {
    protected val _finishEvent = MutableLiveEvent()
    protected val _errorEvent = MutableDataLiveEvent<Int>()
    protected val _messageEvent = MutableDataLiveEvent<Int>()

    val finishEvent = _finishEvent as LiveEvent
    val errorEvent = _errorEvent as DataLiveEvent<Int>
    val messageEvent = _messageEvent as DataLiveEvent<Int>


    open fun attach() {}
    open fun detach() {}

    fun onFinish() {
        _finishEvent.call()
    }

    fun onError(@StringRes errMessageRes: Int) {
        _errorEvent.call(errMessageRes)
    }

    fun onMessage(@StringRes messageRes: Int) {
        _messageEvent.call(messageRes)
    }
}
