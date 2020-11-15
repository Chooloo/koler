package com.chooloo.www.callmanager.ui.settings_fragment;

import android.widget.Toast;

import androidx.preference.Preference;

import com.chooloo.www.callmanager.ui.base.MvpView;

public interface SettingsMvpView extends MvpView {
    void setUp();

    void setListPreferenceSummary(Preference preference, Object newValue);

    void setCheckBoxPreferenceSummary(Preference preferenece, Object newValue);

    void setSwitchPreferenceSummary(Preference preference, Object newValue);

    void setupSimSelection();

    void goToMainActivity();
}
