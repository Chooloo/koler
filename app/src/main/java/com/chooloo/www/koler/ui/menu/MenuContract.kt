package com.chooloo.www.koler.ui.menu

import com.chooloo.www.koler.ui.list.ListContract

interface MenuContract : ListContract {
    interface View : ListContract.View
    interface Presenter<V : View> : ListContract.Presenter<V>
}