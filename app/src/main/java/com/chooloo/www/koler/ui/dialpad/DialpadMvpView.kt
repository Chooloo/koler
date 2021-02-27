package com.chooloo.www.koler.ui.dialpad

import com.chooloo.www.koler.ui.base.MvpView

interface DialpadMvpView : MvpView {
    val isDialer: Boolean
    var number: String
    var isDeleteButtonVisible: Boolean
    var isAddContactButtonVisible: Boolean

    //region phone actions
    fun call()
    fun addContact()
    fun callVoicemail()
    //endregion

    //region key actions
    fun vibrate()
    fun playTone(keyCode: Int)
    fun registerKeyEvent(keyCode: Int)
    //endregion

    fun backspace()
    fun setViewModelNumber(number: String?)
}