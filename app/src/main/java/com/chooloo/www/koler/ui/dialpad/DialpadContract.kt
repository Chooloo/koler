package com.chooloo.www.koler.ui.dialpad

import com.chooloo.www.koler.data.ContactsBundle
import com.chooloo.www.koler.ui.base.BaseContract

interface DialpadContract : BaseContract {
    interface View : BaseContract.View {
        val _isDialer: Boolean
        var number: String
        val suggestionsCount: Int
        var isDeleteButtonVisible: Boolean
        var isAddContactButtonVisible: Boolean

        fun call()
        fun vibrate()
        fun backspace()
        fun addContact()
        fun callVoicemail()
        fun playTone(keyCode: Int)
        fun invokeKey(keyCode: Int)
        fun showSuggestions(isShow: Boolean)
        fun setSuggestionsFilter(filter: String)
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onKeyClick(keyCode: Int)
        fun onLongKeyClick(keyCode: Int): Boolean
        fun onCallClick()
        fun onDeleteClick()
        fun onAddContactClick()
        fun onLongDeleteClick(): Boolean
        fun onTextChanged(text: String?)
        fun onSuggestionsChanged(contactsBundle: ContactsBundle)
    }
}