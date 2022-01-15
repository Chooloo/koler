package com.chooloo.www.kontacts.ui.main

import com.chooloo.www.chooloolib.ui.base.BaseContract

interface MainContract : BaseContract {
    interface View : BaseContract.View {
    }

    interface Controller<V : View> : BaseContract.Controller<V> {
        fun onSettingsClick()
        fun onAddContactClick()
        fun onSearchTextChange(text: String)
        fun onSearchFocusChange(isFocus: Boolean)
    }
}