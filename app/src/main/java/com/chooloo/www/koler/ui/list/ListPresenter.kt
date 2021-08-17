package com.chooloo.www.koler.ui.list

import com.chooloo.www.koler.ui.base.BasePresenter

open class ListPresenter<ItemType, V : ListContract.View<ItemType>>(mvpView: V) :
    BasePresenter<V>(mvpView),
    ListContract.Presenter<ItemType, V> {

    override fun onResults() {
        view.showEmptyPage(false)
    }

    override fun onNoResults() {
        view.apply {
            emptyStateText = view.noResultsMessage
            showEmptyPage(true)
        }
    }

    override fun onDataChanged() {
        view.apply {
            if (itemCount == 0) {
                onNoResults()
            } else {
                onResults()
            }
        }
    }

    override fun onSwipeRefresh() {
        view.apply {
            requestSearchFocus()
            toggleRefreshing(false)
        }
    }

    override fun onPermissionsGranted() {
        view.apply {
            emptyStateText = view.noResultsMessage
            attachData()
        }
    }

    override fun onSearchTextChanged(text: String) {
        view.applyFilter(text)
    }

    override fun onSelectingChanged(isSelecting: Boolean) {
        view.showSelecting(isSelecting)
    }

    override fun onPermissionsBlocked(permissions: Array<String>) {
        view.emptyStateText = view.noPermissionsMessage
    }
}