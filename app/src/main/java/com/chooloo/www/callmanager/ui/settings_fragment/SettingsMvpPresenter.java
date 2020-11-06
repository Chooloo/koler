package com.chooloo.www.callmanager.ui.settings_fragment;

import androidx.preference.Preference;

import com.chooloo.www.callmanager.ui.base.MvpPresenter;

public interface SettingsMvpPresenter<V extends SettingsMvpView> extends MvpPresenter<V> {
    void onRequestPermissionResult(int requestCode, int[] grantResults);

    void refresh();

    boolean onListPreferenceChange(Preference preference, Object newValue);

    boolean onSwitchPreferenceChange(Preference preference, Object newValue);

    boolean onCheckBoxPreferenceChange(Preference preference, Object newValue);
}
