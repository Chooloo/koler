package com.chooloo.www.callmanager.ui.fragment.settings;

import androidx.preference.Preference;

import com.chooloo.www.callmanager.ui.base.BaseContract;

public class SettingsContract implements BaseContract {
    interface View extends BaseContract.View {
        void setListPreferenceSummary(Preference preference, Object newValue);

        void setCheckBoxPreferenceSummary(Preference preferenece, Object newValue);

        void setSwitchPreferenceSummary(Preference preference, Object newValue);

        void setupSimSelection();

        void goToMainActivity();
    }

    interface Presenter<V extends View> extends BaseContract.Presenter<V> {
        void onRequestPermissionResult(int requestCode, int[] grantResults);

        void refresh();

        boolean onListPreferenceChange(Preference preference, Object newValue);

        boolean onSwitchPreferenceChange(Preference preference, Object newValue);

        boolean onCheckBoxPreferenceChange(Preference preference, Object newValue);
    }
}
