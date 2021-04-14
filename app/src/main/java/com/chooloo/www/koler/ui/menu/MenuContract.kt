package com.chooloo.www.koler.ui.menu

import android.view.MenuItem
import com.chooloo.www.koler.ui.list.ListContract

interface MenuContract : ListContract {
    interface View : ListContract.View<MenuItem>
    interface Presenter<V : View> : ListContract.Presenter<MenuItem, V>
}