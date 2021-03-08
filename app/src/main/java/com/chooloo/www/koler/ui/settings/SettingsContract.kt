package com.chooloo.www.koler.ui.settings

import com.chooloo.www.koler.ui.base.BaseContract

interface SettingsContract : BaseContract {
    interface View : BaseContract.View
    interface Presenter<V : View> : BaseContract.Presenter<V>
}