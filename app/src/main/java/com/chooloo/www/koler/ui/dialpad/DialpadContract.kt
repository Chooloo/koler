package com.chooloo.www.koler.ui.dialpad

import com.chooloo.www.koler.ui.base.BaseContract

interface DialpadContract : BaseContract {
    interface View : BaseContract.View {
        val isDialer: Boolean
        var number: String
        var isDeleteButtonVisible: Boolean
        var isAddContactButtonVisible: Boolean

        fun call()
        fun addContact()
        fun callVoicemail()
        fun vibrate()
        fun playTone(keyCode: Int)
        fun registerKeyEvent(keyCode: Int)
        fun backspace()
        fun setViewModelNumber(number: String?)
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onKeyClick(keyCode: Int)
        fun onLongKeyClick(keyCode: Int):Boolean
        fun onCallClick()
        fun onDeleteClick()
        fun onAddContactClick()
        fun onLongDeleteClick(): Boolean
        fun onTextChanged(text: String?)
    }
}