package com.chooloo.www.chooloolib.ui.base.menu

import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.ui.base.BaseViewState

abstract class BaseMenuViewState : BaseViewState() {
    val title = MutableLiveData<String>()
    val subtitle = MutableLiveData<String>()
    abstract val menuResList: List<Int>

    open fun onMenuItemClick(menuItem: MenuItem) {
        onMenuItemClick(menuItem.itemId)
    }

    open fun onMenuItemClick(itemId: Int) {
    }
}