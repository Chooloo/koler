package com.chooloo.www.koler.ui.list

import com.chooloo.www.koler.adapter.ListAdapter
import com.chooloo.www.koler.ui.base.BaseContract

interface ListContract : BaseContract {
    interface View<ItemType> : BaseContract.View {
        val isCompact: Boolean
        val searchHint: String?
        var emptyStateText: String?

        fun animateListView()
        fun requestSearchFocus()
        fun setupScrollIndicator()
        fun showItem(item: ItemType)
        fun showEmptyPage(isShow: Boolean)
        fun showLoading(isLoading: Boolean)
        fun showSelecting(isSelecting: Boolean)
        fun setAdapter(adapter: ListAdapter<ItemType>)
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

        fun setOnItemsChangedListener(onItemsChangedListener: (ArrayList<ItemType>) -> Unit? = {})
    }
}