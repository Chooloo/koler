package com.chooloo.www.koler.ui.menu.contact

import android.view.MenuItem
import com.chooloo.www.koler.ui.base.BaseContract
import com.chooloo.www.koler.ui.menu.MenuContract

interface ContactMenuContract : MenuContract {
    interface View : BaseContract.View {
        fun blockContact()
        fun unblockContact()
        fun setContactFavorite()
        fun unsetContactFavorite()
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onMenuItemClick(menuItem: MenuItem)
    }
}