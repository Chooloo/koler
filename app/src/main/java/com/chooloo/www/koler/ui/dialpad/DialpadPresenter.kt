package com.chooloo.www.koler.ui.dialpad

import android.view.KeyEvent
import com.chooloo.www.koler.ui.base.BasePresenter

class DialpadPresenter<V : DialpadMvpView> : BasePresenter<V>(), DialpadMvpPresenter<V> {
    override fun onKeyClick(keyCode: Int) {
        mvpView?.vibrate()
        mvpView?.playTone(keyCode)
        mvpView?.registerKeyEvent(keyCode)
    }

    override fun onCallClick() {
        mvpView?.call()
    }

    override fun onDeleteClick() {
        mvpView?.backspace()
    }

    override fun onAddContactClick() {
        mvpView?.addContact()
    }

    override fun onLongDeleteClick(): Boolean {
        mvpView?.number = ""
        return true
    }

    override fun onLongOneClick(): Boolean {
        if (mvpView?.isDialer == true) {
            mvpView?.callVoicemail()
        }
        return mvpView?.isDialer ?: false
    }

    override fun onLongZeroClick(): Boolean {
        onKeyClick(KeyEvent.KEYCODE_PLUS)
        return true
    }

    override fun onTextChanged(text: String) {
        mvpView?.showDeleteButton(text.isNotEmpty())
        mvpView?.showAddContactButton(text.isNotEmpty())
        mvpView?.setViewModelNumber(text)
    }
}