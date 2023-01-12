package com.chooloo.www.chooloolib.ui.base.menu

import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.ui.base.BaseViewState

abstract class BaseMenuViewState : BaseViewState() {
    protected open val _title = MutableLiveData<String>()
    protected open val _subtitle = MutableLiveData<String>()

    val title = _title as LiveData<String>
    val subtitle = _subtitle as LiveData<String>

    abstract val menuResList: List<Int>


    open fun onMenuItemClick(menuItem: MenuItem) {
        onMenuItemClick(menuItem.itemId)
    }

    open fun onMenuItemClick(itemId: Int) {
    }
}