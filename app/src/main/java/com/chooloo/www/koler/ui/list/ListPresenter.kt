package com.chooloo.www.koler.ui.list

import com.chooloo.www.koler.ui.base.BasePresenter

open class ListPresenter<ItemType, V : ListContract.View<ItemType>> : BasePresenter<V>(),
    ListContract.Presenter<ItemType, V> {
    override fun onResults() {
        mvpView?.showEmptyPage(false)
    }

    override fun onNoResults() {
        mvpView?.apply {
            emptyStateText = mvpView?.noResultsMessage
            showEmptyPage(true)
        }
    }

    override fun onDataChanged() {
        mvpView?.apply {
            if (itemCount == 0) {
                onNoResults()
            } else {
                onResults()
            }
        }
    }

    override fun onSwipeRefresh() {
        mvpView?.apply {
            requestSearchFocus()
            toggleRefreshing(false)
        }
    }

    override fun onPermissionsGranted() {
        mvpView?.apply {
            emptyStateText = mvpView?.noResultsMessage
            attachData()
        }
    }

    override fun onSearchTextChanged(text: String) {
        mvpView?.applyFilter(text)
    }

    override fun onSelectingChanged(isSelecting: Boolean) {
        mvpView?.showSelecting(isSelecting)
    }

    override fun onPermissionsBlocked(permissions: Array<String>) {
        mvpView?.emptyStateText = mvpView?.noPermissionsMessage
    }
}