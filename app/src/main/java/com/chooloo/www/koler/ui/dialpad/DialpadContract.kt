package com.chooloo.www.koler.ui.dialpad

import com.chooloo.www.koler.data.account.Contact
import com.chooloo.www.koler.ui.base.BaseContract

interface DialpadContract : BaseContract {
    interface View : BaseContract.View {
        var number: String
        val isDialer: Boolean
        val suggestionsCount: Int
        var isSuggestionsVisible: Boolean
        var isDeleteButtonVisible: Boolean
        var isAddContactButtonVisible: Boolean

        fun invokeKey(keyCode: Int)
        fun setSuggestionsFilter(filter: String)
    }

    interface Controller<V : View> : BaseContract.Controller<V> {
        fun onKeyClick(keyCode: Int)
        fun onLongKeyClick(keyCode: Int): Boolean
        fun onCallClick()
        fun onDeleteClick()
        fun onAddContactClick()
        fun onLongDeleteClick(): Boolean
        fun onTextChanged(text: String?)
        fun onSuggestionsChanged(contacts: ArrayList<Contact>)
    }
}