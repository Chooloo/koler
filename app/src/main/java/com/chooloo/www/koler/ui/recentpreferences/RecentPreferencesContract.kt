package com.chooloo.www.koler.ui.recentpreferences

import com.chooloo.www.koler.ui.base.BaseContract

class RecentPreferencesContract : BaseContract {
    interface View : BaseContract.View {
        val number: String?
        var isBlockNumberVisible: Boolean
        var isUnblockNumberVisible: Boolean
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onBlockNumberClick()
        fun onUnblockNumberClick()
    }
}