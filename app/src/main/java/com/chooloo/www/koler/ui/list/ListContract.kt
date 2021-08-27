package com.chooloo.www.koler.ui.list

import com.chooloo.www.koler.ui.base.BaseContract

interface ListContract : BaseContract {
    interface View<ItemType> : BaseContract.View {
        val itemCount: Int
        val searchHint: String?
        var emptyStateText: String?


        fun animateListView()
        fun requestSearchFocus()
        fun setupScrollIndicator()
        fun showItem(item: ItemType)
        fun showEmptyPage(isShow: Boolean)
        fun showSelecting(isSelecting: Boolean)
        fun toggleRefreshing(isRefreshing: Boolean)
        fun updateData(dataList: ArrayList<ItemType>)
    }

    interface Presenter<ItemType, V : View<ItemType>> : BaseContract.Presenter<V> {
        val noResultsMessage: String
        val noPermissionsMessage: String
        val requiredPermissions: Array<String>

        fun onResults()
        fun onNoResults()
        fun onSwipeRefresh()

        fun onItemClick(item: ItemType) {}
        fun onItemLongClick(item: ItemType) {}

        fun onSearchTextChanged(text: String) {}
        fun onDataChanged(items: ArrayList<ItemType>)
        fun onDeleteItems(items: ArrayList<ItemType>) {}
        fun onIsSelectingChanged(isSelecting: Boolean) {}

        fun onPermissionsGranted()
        fun onPermissionsBlocked(permissions: Array<String>)
    }
}