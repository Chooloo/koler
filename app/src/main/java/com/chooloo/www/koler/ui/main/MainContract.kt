package com.chooloo.www.koler.ui.main

import android.content.Intent
import android.view.MenuItem
import com.chooloo.www.koler.ui.base.BaseContract

interface MainContract : BaseContract {
    interface View : BaseContract.View {
        var dialpadNumber: String

        fun openDialpad()
        fun checkIntent()
        fun openSettings()
        fun updateSearchViewModelText(text: String?)
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onDialpadFabClick()
        fun onMenuClick()
        fun onViewIntent(intent: Intent)
        fun onSearchTextChanged(text: String)
    }
}