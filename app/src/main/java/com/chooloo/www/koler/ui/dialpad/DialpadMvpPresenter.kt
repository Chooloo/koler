package com.chooloo.www.koler.ui.dialpad

import com.chooloo.www.koler.ui.base.MvpPresenter

interface DialpadMvpPresenter<V : DialpadMvpView> : MvpPresenter<V> {
    fun onKeyClick(keyCode: Int)
    fun onLongKeyClick(keyCode: Int):Boolean
    fun onCallClick()
    fun onDeleteClick()
    fun onAddContactClick()
    fun onLongDeleteClick(): Boolean
    fun onTextChanged(text: String)
}