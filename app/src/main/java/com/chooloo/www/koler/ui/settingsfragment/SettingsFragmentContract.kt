package com.chooloo.www.koler.ui.settingsfragment

import androidx.preference.Preference
import com.chooloo.www.koler.ui.base.BaseContract

class SettingsFragmentContract : BaseContract {
    interface View : BaseContract.View {
        fun setListPreferenceSummary(preference: Preference, newValue: Any)
        fun setCheckBoxPreferenceSummary(preferenece: Preference, newValue: Any)
        fun setSwitchPreferenceSummary(preference: Preference, newValue: Any)
        fun setupSimSelection()
        fun goToMainActivity()
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onRequestPermissionResult(requestCode: Int, grantResults: IntArray)
        fun onListPreferenceChange(preference: Preference, newValue: Any): Boolean
        fun onSwitchPreferenceChange(preference: Preference, newValue: Any): Boolean
        fun onCheckBoxPreferenceChange(preference: Preference, newValue: Any): Boolean
        fun refresh()
    }
}