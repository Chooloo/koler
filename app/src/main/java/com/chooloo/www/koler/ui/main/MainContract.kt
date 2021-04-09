package com.chooloo.www.koler.ui.main

import android.content.Intent
import com.chooloo.www.koler.ui.base.BaseContract

interface MainContract : BaseContract {
    interface View : BaseContract.View {
        var selectedPage: Int
        var searchText: String?
        var dialpadNumber: String
        var liveContactsText: String?
        var liveRecentsText: String?

        fun openDialpad()
        fun checkIntent()
        fun openSettings()
        fun updateSearchViewModelNumber(text: String?)
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onMenuClick()
        fun onDialpadFabClick()
        fun onViewIntent(intent: Intent)
        fun onPageSelected(position: Int)
        fun onSearchTextChanged(text: String)
        fun onDialpadTextChanged(text: String?)
    }
}