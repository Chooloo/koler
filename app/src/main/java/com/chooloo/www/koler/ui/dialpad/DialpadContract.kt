package com.chooloo.www.koler.ui.dialpad

import com.chooloo.www.koler.data.account.ContactAccount
import com.chooloo.www.koler.ui.base.BaseContract

interface DialpadContract : BaseContract {
    interface View : BaseContract.View {
        var text: String
        var isDeleteButtonVisible: Boolean

        fun invokeKey(keyCode: Int)
    }

    interface Controller<V : View> : BaseContract.Controller<V> {
        fun onKeyClick(keyCode: Int)
        fun onLongKeyClick(keyCode: Int): Boolean
        fun onDeleteClick()
        fun onLongDeleteClick(): Boolean
        fun onTextChanged(text: String?)
    }
}