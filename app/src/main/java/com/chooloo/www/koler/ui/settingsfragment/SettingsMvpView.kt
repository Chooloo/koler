package com.chooloo.www.koler.ui.settingsfragment

import androidx.preference.Preference
import com.chooloo.www.koler.ui.base.MvpView

interface SettingsMvpView : MvpView {

    override fun onSetup()

    fun setListPreferenceSummary(preference: Preference, newValue: Any)
    fun setCheckBoxPreferenceSummary(preferenece: Preference, newValue: Any)
    fun setSwitchPreferenceSummary(preference: Preference, newValue: Any)

    fun setupSimSelection()
    fun goToMainActivity()
}