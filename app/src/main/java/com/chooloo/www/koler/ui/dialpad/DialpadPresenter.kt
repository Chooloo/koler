package com.chooloo.www.koler.ui.dialpad

import android.view.KeyEvent.*
import com.chooloo.www.koler.data.Contact
import com.chooloo.www.koler.ui.base.BasePresenter

class DialpadPresenter<V : DialpadContract.View>(mvpView: V) :
    BasePresenter<V>(mvpView),
    DialpadContract.Presenter<V> {

    override fun onKeyClick(keyCode: Int) {
        view.apply {
            vibrate()
            playTone(keyCode)
            invokeKey(keyCode)
        }
    }

    override fun onLongKeyClick(keyCode: Int): Boolean {
        return when (keyCode) {
            KEYCODE_0 -> {
                onKeyClick(KEYCODE_PLUS)
                true
            }
            KEYCODE_1 -> {
                if (view.isDialer) view.callVoicemail()
                view.isDialer
            }
            else -> true
        }
    }

    override fun onCallClick() {
        view.call()
    }

    override fun onDeleteClick() {
        view.backspace()
    }

    override fun onAddContactClick() {
        view.addContact()
    }

    override fun onLongDeleteClick(): Boolean {
        view.number = ""
        return true
    }

    override fun onTextChanged(text: String?) {
        view.apply {
            if (isDialer) {
                isDeleteButtonVisible = text?.isNotEmpty() == true
                isAddContactButtonVisible = text?.isNotEmpty() == true
                setSuggestionsFilter(text ?: "")
            }
        }
    }

    override fun onSuggestionsChanged(contacts: ArrayList<Contact>) {
        view.apply {
            isSuggestionsVisible = contacts.isNotEmpty() && number.isNotEmpty() == true
        }
    }
}