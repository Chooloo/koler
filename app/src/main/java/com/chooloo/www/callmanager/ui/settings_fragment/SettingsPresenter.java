package com.chooloo.www.callmanager.ui.settings_fragment;

import androidx.preference.Preference;

import com.chooloo.www.callmanager.ui.base.BasePresenter;
import com.chooloo.www.callmanager.util.PermissionUtils;

import static com.chooloo.www.callmanager.util.PermissionUtils.RC_DEFAULT;

public class SettingsPresenter<V extends SettingsMvpView> extends BasePresenter<V> implements SettingsMvpPresenter<V> {

    @Override
    public void onRequestPermissionResult(int requestCode, int[] grantResults) {
        if (requestCode == RC_DEFAULT && PermissionUtils.checkPermissionsGranted(grantResults)) {
            mvpView.setupSimSelection();
        }
    }

    @Override
    public void refresh() {
        mvpView.goToMainActivity();
    }

    @Override
    public boolean onListPreferenceChange(Preference preference, Object newValue) {
        mvpView.setListPreferenceSummary(preference, newValue);
        return true;
    }

    @Override
    public boolean onSwitchPreferenceChange(Preference preference, Object newValue) {
        mvpView.setSwitchPreferenceSummary(preference, newValue);
        return true;
    }

    @Override
    public boolean onCheckBoxPreferenceChange(Preference preference, Object newValue) {
        mvpView.setCheckBoxPreferenceSummary(preference, newValue);
        return true;
    }
}
