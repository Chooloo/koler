package com.chooloo.www.koler.ui.menu.recent

import android.view.MenuItem
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BasePresenter

class RecentMenuPresenter<V : RecentMenuContract.View> : BasePresenter<V>(),
    RecentMenuContract.Presenter<V> {
    override fun onMenuItemClick(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.recent_extra_block -> mvpView?.blockNumber()
            R.id.recent_extra_unblock -> mvpView?.unblockNumber()
        }
    }
}