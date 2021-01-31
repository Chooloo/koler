package com.chooloo.www.callmanager.ui.dialpad

import android.view.KeyEvent
import com.chooloo.www.callmanager.ui.base.BasePresenter

class DialpadPresenter<V : DialpadMvpView?> : BasePresenter<V>(), DialpadMvpPresenter<V> {
    override fun onKeyClick(keyCode: Int) {
        mMvpView?.vibrate()
        mMvpView?.playTone(keyCode)
        mMvpView?.registerKeyEvent(keyCode)
    }

    override fun onCallClick() {
        mMvpView?.call()
    }

    override fun onDeleteClick() {
        mMvpView?.backspace()
    }

    override fun onAddContactClick() {
        mMvpView?.addContact()
    }

    override fun onLongDeleteClick(): Boolean {
        mMvpView?.number = ""
        return true
    }

    override fun onLongOneClick(): Boolean {
        if (mMvpView?.isDialer == true) {
            mMvpView?.callVoicemail()
        }
        return mMvpView?.isDialer ?: false
    }

    override fun onLongZeroClick(): Boolean {
        onKeyClick(KeyEvent.KEYCODE_PLUS)
        return true
    }

    override fun onTextChanged(text: String) {
        mMvpView?.showDeleteButton(text.isNotEmpty())
        mMvpView?.showAddContactButton(text.isNotEmpty())
        mMvpView?.setViewModelNumber(text)
    }
}