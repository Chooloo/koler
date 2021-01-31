package com.chooloo.www.callmanager.ui.dialpad

import com.chooloo.www.callmanager.ui.base.MvpView

interface DialpadMvpView : MvpView {
    val isDialer: Boolean
    var number: String

    fun showDeleteButton(isShow: Boolean)
    fun showAddContactButton(isShow: Boolean)
    fun toggleDialerView(isToggle: Boolean)
    fun registerKeyEvent(keyCode: Int)
    fun setViewModelNumber(number:String?)
    fun backspace()
    fun call()
    fun callVoicemail()
    fun addContact()
    fun vibrate()
    fun playTone(keyCode: Int)
}