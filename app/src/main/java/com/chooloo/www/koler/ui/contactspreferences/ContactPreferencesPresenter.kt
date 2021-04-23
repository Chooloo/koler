package com.chooloo.www.koler.ui.contactspreferences

import com.chooloo.www.koler.ui.base.BasePresenter

class ContactPreferencesPresenter<V : ContactPreferencesContract.View> : BasePresenter<V>(),
    ContactPreferencesContract.Presenter<V> {
    override fun onBlockClick() {
        mvpView?.toggleContactBlocked(true)
    }

    override fun onUnblockClick() {
        mvpView?.toggleContactBlocked(false)
    }

    override fun onFavoriteClick() {
        mvpView?.toggleContactFavorite(true)
    }

    override fun onUnFavoriteClick() {
        mvpView?.toggleContactFavorite(false)
    }
}