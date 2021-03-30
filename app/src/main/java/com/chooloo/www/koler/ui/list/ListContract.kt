package com.chooloo.www.koler.ui.list

import com.chooloo.www.koler.ui.base.BaseContract

interface ListContract : BaseContract {
    interface View : BaseContract.View {
        val itemCount: Int
        var emptyStateText: String?
        val noResultsMessage: String
        val noPermissionsMessage: String
        val requiredPermissions: Array<String>?

        fun attachData()
        fun animateListView()
        fun showEmptyPage(isShow: Boolean)
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onResults()
        fun onNoResults()
        fun onPermissionsGranted()
        fun onPermissionsBlocked(permissions: Array<String>)
    }
}