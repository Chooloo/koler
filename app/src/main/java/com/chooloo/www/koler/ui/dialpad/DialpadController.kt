package com.chooloo.www.koler.ui.dialpad

import android.view.KeyEvent.*
import androidx.lifecycle.ViewModelProvider
import com.chooloo.www.koler.R
import com.chooloo.www.koler.data.account.Contact
import com.chooloo.www.koler.interactor.audio.AudioInteractor
import com.chooloo.www.koler.ui.base.BaseController
import com.chooloo.www.koler.viewmodel.DialpadViewModel

class DialpadController<V : DialpadContract.View>(view: V) :
    BaseController<V>(view),
    DialpadContract.Controller<V> {

    private val _searchViewModel by lazy {
        ViewModelProvider(boundComponent.viewModelStoreOwner).get(DialpadViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        if (view.isDialer) {
            _searchViewModel.number.observe(boundComponent.lifecycleOwner) {
                it?.let { view.number = it }
            }
        }
    }

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
        boundComponent.audioInteractor.vibrate()
    }

    override fun onAddContactClick() {
        boundComponent.navigationInteractor.goToAddContact(view.number)
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