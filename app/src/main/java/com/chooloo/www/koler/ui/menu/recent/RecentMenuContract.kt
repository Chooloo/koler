package com.chooloo.www.koler.ui.menu.recent

import android.view.MenuItem
import com.chooloo.www.koler.ui.base.BaseContract
import com.chooloo.www.koler.ui.menu.MenuContract

interface RecentMenuContract : MenuContract {
    interface View : BaseContract.View {
        fun blockNumber()
        fun unblockNumber()
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onMenuItemClick(menuItem: MenuItem)
    }
}