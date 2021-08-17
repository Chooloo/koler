package com.chooloo.www.koler.ui.contactspreferences

import com.chooloo.www.koler.ui.base.BasePresenter

class ContactPreferencesPresenter<V : ContactPreferencesContract.View>(mvpView: V) :
    BasePresenter<V>(mvpView),
    ContactPreferencesContract.Presenter<V> {

    override fun onBlockClick() {
        view.toggleContactBlocked(true)
    }

    override fun onUnblockClick() {
        view.toggleContactBlocked(false)
    }

    override fun onFavoriteClick() {
        view.toggleContactFavorite(true)
    }

    override fun onUnFavoriteClick() {
        view.toggleContactFavorite(false)
    }
}