package com.chooloo.www.koler.ui.settings

import com.chooloo.www.koler.ui.base.BaseContract
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractor.Companion.AccentTheme
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractor.Companion.Page
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractor.Companion.RecordFormat
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractor.Companion.Sim

class SettingsContract : BaseContract {
    interface View : BaseContract.View {
        //region pref
        fun setPrefSim(sim: Sim)
        fun setPrefDefaultPage(page: Page)
        fun setPrefCompact(isCompact: Boolean)
        fun setPrefAnimations(isAnimations: Boolean)
        fun setPrefRecordsEnabled(isEnabled: Boolean)
        fun setPrefScrollIndicator(isEnabled: Boolean)
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
        fun onClickedRate()
        fun onClickedEmail()
        fun onClickedColor()
        fun onClickedDonate()
        fun onClickedReport()
        fun onClickedManageBlocked()
        fun onSelectedColor(color: Int)
        fun onSelectedSim(newValue: Any?)
        fun onToggledRecords(isToggle: Boolean)
        fun onToggledAnimation(isToggle: Boolean)
        fun onSelectedDefaultPage(pageKey: String)
        fun onSelectedRecordFormat(newValue: Any?)
        fun onToggledCompactMode(isToggle: Boolean)
        fun onToggledScrollIndicator(isToggle: Boolean)
    }
}