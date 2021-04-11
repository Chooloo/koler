package com.chooloo.www.koler.ui.settings

import com.chooloo.www.koler.ui.base.BaseContract
import com.chooloo.www.koler.util.preferences.KolerPreferences.Companion.AccentTheme
import com.chooloo.www.koler.util.preferences.KolerPreferences.Companion.Page
import com.chooloo.www.koler.util.preferences.KolerPreferences.Companion.RecordFormat
import com.chooloo.www.koler.util.preferences.KolerPreferences.Companion.Sim

class SettingsContract : BaseContract {
    interface View : BaseContract.View {
        //region pref
        fun setPrefSim(sim: Sim)
        fun setPrefDefaultPage(page: Page)
        fun setPrefCompact(isCompact: Boolean)
        fun setPrefAnimations(isAnimations: Boolean)
        fun setPrefAccentTheme(accentTheme: AccentTheme)
        fun setPrefRecordFormat(recordFormat: RecordFormat)
        //endregion

        fun donate()
        fun rateApp()
        fun reportBug()
        fun sendEmail()
        fun openSource()
        fun openColorPicker()
        fun goToMainActivity()
        fun setupSimPreference()
        fun manageBlockedNumbers()
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun refresh()
        fun onClickedRate(): Boolean
        fun onClickedEmail(): Boolean
        fun onClickedColor(): Boolean
        fun onClickedDonate(): Boolean
        fun onClickedReport(): Boolean
        fun onManageBlocked(): Boolean
        fun onSelectedColor(color: Int): Boolean
        fun onSelectedSim(newValue: Any?): Boolean
        fun onToggledAnimation(isToggle: Boolean): Boolean
        fun onSelectedDefaultPage(pageKey: String): Boolean
        fun onSelectedRecordFormat(newValue: Any?): Boolean
        fun onToggledCompactMode(isToggle: Boolean): Boolean
    }
}