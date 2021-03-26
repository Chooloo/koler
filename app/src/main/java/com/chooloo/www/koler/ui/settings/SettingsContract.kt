package com.chooloo.www.koler.ui.settings

import com.chooloo.www.koler.ui.base.BaseContract
import com.chooloo.www.koler.util.preferences.KolerPreferences.Companion.AccentTheme
import com.chooloo.www.koler.util.preferences.KolerPreferences.Companion.RecordFormat
import com.chooloo.www.koler.util.preferences.KolerPreferences.Companion.Sim

class SettingsContract : BaseContract {
    interface View : BaseContract.View {
        fun setPrefSim(sim: Sim)
        fun setPrefAccentTheme(accentTheme: AccentTheme)
        fun setPrefRecordFormat(recordFormat: RecordFormat)
        fun setupSimPreference()
        fun goToMainActivity()
        fun openColorPicker()
        fun openSource()
        fun sendEmail()
        fun reportBug()
        fun rateApp()
        fun donate()
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onClickedRate(): Boolean
        fun onClickedEmail(): Boolean
        fun onClickedColor(): Boolean
        fun onClickedDonate(): Boolean
        fun onClickedReport(): Boolean
        fun onSelectedColor(color: Int): Boolean
        fun onSelectedSim(newValue: Any?): Boolean
        fun onSelectedRecordFormat(newValue: Any?): Boolean
        fun refresh()
    }
}