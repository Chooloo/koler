package com.chooloo.www.koler.ui.settings

import android.view.MenuItem
import com.chooloo.www.chooloolib.ui.base.BaseContract

interface SettingsContract : BaseContract {
    interface View : BaseContract.View {
    }

    interface Controller<V : View> : BaseContract.Controller<V> {
        fun onMenuItemClick(menuItem: MenuItem)
    }
}