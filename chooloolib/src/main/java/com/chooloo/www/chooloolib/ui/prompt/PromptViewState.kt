package com.chooloo.www.chooloolib.ui.prompt

import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.ui.base.BaseViewState
import com.chooloo.www.chooloolib.util.DataLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PromptViewState @Inject constructor() : BaseViewState() {
    val title = MutableLiveData<String>()
    val subtitle = MutableLiveData<String>()
    val itemClickedEvent = DataLiveEvent<Boolean>()


    fun onNoClick() {
        itemClickedEvent.call(false)
        finishEvent.call()
    }

    fun onYesClick() {
        itemClickedEvent.call(true)
        finishEvent.call()
    }
}