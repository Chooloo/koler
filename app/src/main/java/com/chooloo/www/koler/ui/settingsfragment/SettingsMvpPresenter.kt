package com.chooloo.www.koler.ui.settingsfragment

import androidx.preference.Preference
import com.chooloo.www.koler.ui.base.MvpPresenter

interface SettingsMvpPresenter<V : SettingsMvpView> : MvpPresenter<V> {
    fun onRequestPermissionResult(requestCode: Int, grantResults: IntArray)
    fun onListPreferenceChange(preference: Preference, newValue: Any): Boolean
    fun onSwitchPreferenceChange(preference: Preference, newValue: Any): Boolean
    fun onCheckBoxPreferenceChange(preference: Preference, newValue: Any): Boolean
    fun refresh()
}