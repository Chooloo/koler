package com.chooloo.www.koler.ui.dialpad

import android.view.KeyEvent.*
import com.chooloo.www.koler.ui.base.BasePresenter

class DialpadPresenter<V : DialpadContract.View> : BasePresenter<V>(),
    DialpadContract.Presenter<V> {
    override fun onKeyClick(keyCode: Int) {
        mvpView?.vibrate()
        mvpView?.playTone(keyCode)
        mvpView?.registerKeyEvent(keyCode)
    }

    override fun onLongKeyClick(keyCode: Int) = when (keyCode) {
        KEYCODE_0 -> {
            onKeyClick(KEYCODE_PLUS)
            true
        }
        KEYCODE_1 -> {
            if (mvpView?.isDialer == true) mvpView?.callVoicemail()
            mvpView?.isDialer ?: false
        }
        else -> true
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

    override fun onTextChanged(text: String) {
        mvpView?.apply {
            if (isDialer) {
                isDeleteButtonVisible = text.isNotEmpty()
                isAddContactButtonVisible = text.isNotEmpty()
                setViewModelNumber(text)
            }
        }
    }
}