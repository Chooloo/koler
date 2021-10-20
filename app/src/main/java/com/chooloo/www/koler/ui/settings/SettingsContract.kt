package com.chooloo.www.koler.ui.settings

import com.chooloo.www.koler.ui.base.BaseContract

class SettingsContract : BaseContract {
    interface View : BaseContract.View {
        fun openColorPicker()
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun refresh()

        fun onClickedRate()
        fun onClickedEmail()
        fun onClickedColor()
        fun onClickedDonate()
        fun onClickedReport()
        fun onClickedManageBlocked()

        fun onSelectedColor(color: Int)
        fun onSelectedDefaultPage(pageKey: String)

        fun onToggledAskSim(isToggle: Boolean)
        fun onToggledRecords(isToggle: Boolean)
        fun onToggledAnimation(isToggle: Boolean)
        fun onToggledCompactMode(isToggle: Boolean)
        fun onToggledScrollIndicator(isToggle: Boolean)
    }
}