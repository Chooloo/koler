package com.chooloo.www.koler.ui.settings

import com.chooloo.www.koler.ui.base.BaseContract

class SettingsContract : BaseContract {
    interface View : BaseContract.View {
        fun setupSimSelection()
        fun goToMainActivity()
        fun setAppColor(color: Int)
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onSimSelectionChanged(newValue: Any?)
        fun onAppThemeSelectionChanged(color: Int)
        fun refresh()
    }
}