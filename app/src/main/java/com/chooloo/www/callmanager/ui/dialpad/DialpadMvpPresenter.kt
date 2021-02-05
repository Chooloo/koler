package com.chooloo.www.callmanager.ui.dialpad

import com.chooloo.www.callmanager.ui.base.MvpPresenter

interface DialpadMvpPresenter<V : DialpadMvpView> : MvpPresenter<V> {
    fun onKeyClick(keyCode: Int)
    fun onCallClick()
    fun onDeleteClick()
    fun onAddContactClick()
    fun onLongDeleteClick(): Boolean
    fun onLongOneClick(): Boolean
    fun onLongZeroClick(): Boolean
    fun onTextChanged(text: String)
}