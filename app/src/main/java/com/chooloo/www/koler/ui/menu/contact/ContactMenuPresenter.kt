package com.chooloo.www.koler.ui.menu.contact

import android.view.MenuItem
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BasePresenter

class ContactMenuPresenter<V : ContactMenuContract.View> : BasePresenter<V>(),
    ContactMenuContract.Presenter<V> {
    override fun onMenuItemClick(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.contact_extra_block -> mvpView?.blockContact()
            R.id.contact_extra_unblock -> mvpView?.unblockContact()
            R.id.contact_extra_like -> mvpView?.apply {
                setContactFavorite()
                showMessage(R.string.contact_set_favorite)
            }
            R.id.contact_extra_unlike -> mvpView?.apply {
                unsetContactFavorite()
                showMessage(R.string.contact_unset_favorite)
            }
        }
    }
}