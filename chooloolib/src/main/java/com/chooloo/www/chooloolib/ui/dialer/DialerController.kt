package com.chooloo.www.chooloolib.ui.dialer

import android.view.KeyEvent.KEYCODE_1
import com.chooloo.www.chooloolib.data.account.ContactAccount
import com.chooloo.www.chooloolib.interactor.audio.AudioInteractor
import com.chooloo.www.chooloolib.interactor.navigation.NavigationInteractor
import com.chooloo.www.chooloolib.interactor.recents.RecentsInteractor
import com.chooloo.www.chooloolib.ui.dialpad.DialpadController
import javax.inject.Inject

class DialerController<V : DialerContract.View> @Inject constructor(
    view: V,
    audioInteractor: AudioInteractor,
    private val recentsInteractor: RecentsInteractor,
    private val navigationInteractor: NavigationInteractor
) :
    DialpadController<V>(view, audioInteractor),
    DialerContract.Controller<V> {

    override fun onStart() {
        super.onStart()
        view.isSuggestionsVisible = false
    }

    override fun onLongKeyClick(keyCode: Int): Boolean {
        return if (keyCode == KEYCODE_1) {
            navigationInteractor.callVoicemail()
            true
        } else {
            super.onLongKeyClick(keyCode)
        }
    }

    override fun onCallClick() {
        if (view.text.isEmpty()) {
            view.text = recentsInteractor.getLastOutgoingCall()
        } else {
            navigationInteractor.call(view.text)
        }
    }

    override fun onAddContactClick() {
        navigationInteractor.addContact(view.text)
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