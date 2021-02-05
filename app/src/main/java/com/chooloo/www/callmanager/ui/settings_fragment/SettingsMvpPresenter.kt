package com.chooloo.www.callmanager.ui.settings_fragment

import androidx.preference.Preference
import com.chooloo.www.callmanager.ui.base.MvpPresenter

interface SettingsMvpPresenter<V : SettingsMvpView> : MvpPresenter<V> {

    fun onRequestPermissionResult(requestCode: Int, grantResults: IntArray)

    fun onListPreferenceChange(preference: Preference, newValue: Any): Boolean
    fun onSwitchPreferenceChange(preference: Preference, newValue: Any): Boolean
    fun onCheckBoxPreferenceChange(preference: Preference, newValue: Any): Boolean

    fun refresh()
}