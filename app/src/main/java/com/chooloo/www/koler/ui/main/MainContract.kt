package com.chooloo.www.koler.ui.main

import android.content.Intent
import com.chooloo.www.koler.ui.base.BaseContract

interface MainContract : BaseContract {
    interface View : BaseContract.View {
        var dialpadNumber: String

        fun openDialpad()
        fun checkIntent()
        fun openSettings()
        fun updateSearchViewModelText(text: String?)
        fun updateSearchViewModelNumber(text:String?)
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onMenuClick()
        fun onDialpadFabClick()
        fun onViewIntent(intent: Intent)
        fun onSearchTextChanged(text: String)
        fun onDialpadTextChanged(text: String?)
    }
}