package com.chooloo.www.koler.ui.main

import android.content.Intent
import android.view.MenuItem
import com.chooloo.www.koler.ui.base.BaseContract

interface MainContract : BaseContract {
    interface View : BaseContract.View {
        var dialpadNumber: String

        fun openSettings()
        fun openDialpad()
        fun updateSearchViewModelText(text: String?)
        fun checkIntent()
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onDialpadFabClick()
        fun onMenuClick()
        fun onViewIntent(intent: Intent)
        fun onSearchTextChanged(text: String)
    }
}