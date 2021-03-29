package com.chooloo.www.koler.ui.list

import com.chooloo.www.koler.ui.base.BaseContract

interface ListContract : BaseContract {
    interface View : BaseContract.View {
        val itemCount: Int
        var emptyMessage: String?

        fun showEmptyPage(isShow: Boolean)
        fun animateListView()
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onResults()
        fun onNoResults()
        fun onNoPermissions(permissions: Array<String>)
    }
}