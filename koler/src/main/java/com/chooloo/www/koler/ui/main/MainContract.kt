package com.chooloo.www.koler.ui.main

import android.content.Intent
import com.chooloo.www.chooloolib.data.account.ContactAccount
import com.chooloo.www.chooloolib.data.account.RecentAccount
import com.chooloo.www.chooloolib.ui.base.BaseContract
import com.chooloo.www.chooloolib.ui.base.BaseFragment

interface MainContract : BaseContract {
    interface View : BaseContract.View {
        var searchText: String?
        var currentPageIndex: Int
        var headers: Array<String>

        fun checkIntent()
        fun showSearching()
        fun setSearchHint(hint: String)
        fun setFragmentsAdapter(count: Int, adapter: (position: Int) -> BaseFragment)
    }

    interface Controller : BaseContract.Controller<View> {
        fun onMenuClick()
        fun onDialpadFabClick()
        fun onPageChange(position: Int)
        fun onViewIntent(intent: Intent)
        fun onSearchTextChange(text: String)
        fun onSearchFocusChange(isFocus: Boolean)
        fun onRecentItemClick(recent: RecentAccount)
        fun onContactItemClick(contact: ContactAccount)
    }
}