package com.chooloo.www.chooloolib.ui.dialpad

import com.chooloo.www.chooloolib.ui.base.BaseContract

interface DialpadContract : BaseContract {
    interface View : BaseContract.View {
        var text: String
        var isDeleteButtonVisible: Boolean

        fun invokeKey(keyCode: Int)
    }

    interface Controller : BaseContract.Controller<View> {
        fun onKeyClick(keyCode: Int)
        fun onLongKeyClick(keyCode: Int): Boolean
        fun onDeleteClick()
        fun onLongDeleteClick(): Boolean
        fun onTextChanged(text: String?)
    }
}