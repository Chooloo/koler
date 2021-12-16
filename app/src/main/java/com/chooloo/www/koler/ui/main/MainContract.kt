package com.chooloo.www.koler.ui.main

import android.content.Intent
import com.chooloo.www.koler.ui.base.BaseContract
import com.chooloo.www.koler.ui.base.BaseFragment

interface MainContract : BaseContract {
    interface View : BaseContract.View {
        var currentPageIndex: Int
        var headers: Array<String>

        fun checkIntent()
        fun openSettings()
        fun showSearching()
        fun setSearchHint(hint: String)
        fun openDialer(text: String? = null)
        fun setFragmentsAdapter(count: Int, adapter: (position: Int) -> BaseFragment)
    }

    interface Controller<V : View> : BaseContract.Controller<V> {
        fun onMenuClick()
        fun onDialpadFabClick()
        fun onViewIntent(intent: Intent)
        fun onSearchTextChange(text: String)
        fun onSearchFocusChange(isFocus: Boolean)
    }
}