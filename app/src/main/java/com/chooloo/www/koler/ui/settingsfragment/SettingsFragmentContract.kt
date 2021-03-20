package com.chooloo.www.koler.ui.settingsfragment

import com.chooloo.www.koler.ui.base.BaseContract

class SettingsFragmentContract : BaseContract {
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