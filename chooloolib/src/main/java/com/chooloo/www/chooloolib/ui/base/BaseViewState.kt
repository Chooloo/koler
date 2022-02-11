package com.chooloo.www.chooloolib.ui.base

import androidx.lifecycle.ViewModel
import com.chooloo.www.chooloolib.util.DataLiveEvent
import com.chooloo.www.chooloolib.util.LiveEvent

open class BaseViewState() : ViewModel() {
    val finishEvent = LiveEvent()
    val errorEvent = DataLiveEvent<Int>()
    val messageEvent = DataLiveEvent<Int>()

    open fun attach() {}
    open fun detach() {}
}
