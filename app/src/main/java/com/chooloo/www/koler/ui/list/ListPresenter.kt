package com.chooloo.www.koler.ui.list

import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BasePresenter

open class ListPresenter<V : ListContract.View> : BasePresenter<V>(), ListContract.Presenter<V> {
    override fun onResults() {
        mvpView?.showEmptyPage(false)
    }

    override fun onNoResults() {
        mvpView?.showEmptyPage(true)
    }

    override fun onNoPermissions(permissions: Array<String>) {
        mvpView?.emptyMessage = mvpView?.getString(R.string.error_no_permissions)
        mvpView?.showEmptyPage(true)
    }
}