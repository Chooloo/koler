package com.chooloo.www.koler.ui.list

import com.chooloo.www.koler.data.ListBundle
import com.chooloo.www.koler.ui.base.BaseContract

interface ListContract : BaseContract {
    interface View<ItemType> : BaseContract.View {
        val itemCount: Int
        val searchHint: String?
        val hideNoResults: Boolean
        var emptyStateText: String?
        val noResultsMessage: String
        val noPermissionsMessage: String
        val requiredPermissions: Array<String>?

        fun attachData()
        fun animateListView()
        fun requestSearchFocus()
        fun applyFilter(filter: String) {}
        fun showEmptyPage(isShow: Boolean)
        fun updateData(data: ListBundle<ItemType>)
        fun toggleRefreshing(isRefreshing: Boolean)
    }

    interface Presenter<ItemType, V : View<ItemType>> : BaseContract.Presenter<V> {
        fun onResults()
        fun onNoResults()
        fun onDataChanged()
        fun onSwipeRefresh()
        fun onPermissionsGranted()
        fun onSearchTextChanged(text: String)
        fun onPermissionsBlocked(permissions: Array<String>)
    }
}