package com.chooloo.www.chooloolib.ui.dialer

import android.view.KeyEvent.KEYCODE_1
import com.chooloo.www.chooloolib.data.account.ContactAccount
import com.chooloo.www.chooloolib.ui.dialpad.DialpadController

class DialerController<V : DialerContract.View>(view: V) :
    DialpadController<V>(view),
    DialerContract.Controller<V> {

    override fun onStart() {
        super.onStart()
        view.isSuggestionsVisible = false
    }

    override fun onLongKeyClick(keyCode: Int): Boolean {
        return if (keyCode == KEYCODE_1) {
            component.navigations.callVoicemail()
            true
        } else {
            super.onLongKeyClick(keyCode)
        }
    }

    override fun onCallClick() {
        if (view.text.isEmpty()) {
            view.text = component.recents.getLastOutgoingCall()
        } else {
            component.navigations.call(view.text)
        }
    }

    override fun onAddContactClick() {
        component.navigations.addContact(view.text)
    }

    override fun onTextChanged(text: String?) {
        super.onTextChanged(text)
        view.apply {
            isAddContactButtonVisible = text?.isNotEmpty() == true
            setSuggestionsFilter(text ?: "")
        }
    }

    override fun onSuggestionsChanged(contacts: List<ContactAccount>) {
        view.isSuggestionsVisible = contacts.isNotEmpty() && view.text.isNotEmpty() == true
    }
}