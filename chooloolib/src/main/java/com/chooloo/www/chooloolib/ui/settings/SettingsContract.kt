package com.chooloo.www.chooloolib.ui.settings

import android.view.MenuItem
import com.chooloo.www.chooloolib.ui.base.BaseContract

interface SettingsContract : BaseContract {
    interface View : BaseContract.View {
        fun setMenuResList(menuResList: List<Int>)
    }

    interface Controller : BaseContract.Controller<View> {
        fun onMenuItemClick(menuItem: MenuItem)
    }
}