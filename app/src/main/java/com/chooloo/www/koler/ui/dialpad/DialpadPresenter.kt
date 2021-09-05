package com.chooloo.www.koler.ui.dialpad

import android.view.KeyEvent.*
import com.chooloo.www.koler.R
import com.chooloo.www.koler.data.account.Contact
import com.chooloo.www.koler.interactor.audio.AudioInteractor
import com.chooloo.www.koler.ui.base.BasePresenter

class DialpadPresenter<V : DialpadContract.View>(view: V) :
    BasePresenter<V>(view),
    DialpadContract.Presenter<V> {

    override fun onKeyClick(keyCode: Int) {
        boundComponent.audioInteractor.vibrate(AudioInteractor.SHORT_VIBRATE_LENGTH)
        boundComponent.audioInteractor.playToneByKey(keyCode)
        view.invokeKey(keyCode)
    }

    override fun onLongKeyClick(keyCode: Int): Boolean {
        return when (keyCode) {
            KEYCODE_0 -> {
                onKeyClick(KEYCODE_PLUS)
                true
            }
            KEYCODE_1 -> {
                if (view.isDialer) {
                    boundComponent.navigationInteractor.callVoicemail()
                }
                view.isDialer
            }
            else -> true
        }
    }

    override fun onCallClick() {
        if (view.number.isEmpty()) {
            view.showMessage(R.string.error_enter_number)
        } else {
            boundComponent.navigationInteractor.call(view.number)
        }
    }

    override fun onDeleteClick() {
        view.invokeKey(KEYCODE_DEL)
    }

    override fun onAddContactClick() {
        boundComponent.contactsInteractor.openAddContactView(view.number)
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
        view.isSuggestionsVisible = contacts.isNotEmpty() && view.number.isNotEmpty() == true
    }
}