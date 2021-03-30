package com.chooloo.www.koler.ui.list

import com.chooloo.www.koler.ui.base.BasePresenter

open class ListPresenter<V : ListContract.View> : BasePresenter<V>(), ListContract.Presenter<V> {
    override fun onResults() {
        mvpView?.showEmptyPage(false)
    }

    override fun onNoResults() {
        mvpView?.apply {
            emptyStateText = mvpView?.noResultsMessage
            showEmptyPage(true)
        }
    }

    override fun onPermissionsGranted() {
        mvpView?.apply {
            emptyStateText = mvpView?.noResultsMessage
            attachData()
        }
    }

    override fun onPermissionsBlocked(permissions: Array<String>) {
        mvpView?.emptyStateText = mvpView?.noPermissionsMessage
    }
}