package com.chooloo.www.callmanager.ui.fragment.settings;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;

import com.chooloo.www.callmanager.ui.base.BaseContract;
import com.chooloo.www.callmanager.ui.base.BasePresenter;
import com.chooloo.www.callmanager.util.PermissionUtils;

public class SettingsPresenter<V extends SettingsContract.View> extends BasePresenter<V> implements SettingsContract.Presenter<V> {

    @Override
    public void onRequestPermissionResult(int requestCode, int[] grantResults) {
        if (requestCode == PermissionUtils.PERMISSION_RC && PermissionUtils.checkPermissionsGranted(grantResults)) {
            mView.setupSimSelection();
        }
    }

    @Override
    public void refresh() {
        mView.goToMainActivity();
    }

    @Override
    public boolean onListPreferenceChange(Preference preference, Object newValue) {
        mView.setListPreferenceSummary(preference, newValue);
        return true;
    }

    @Override
    public boolean onSwitchPreferenceChange(Preference preference, Object newValue) {
        mView.setSwitchPreferenceSummary(preference, newValue);
        return true;
    }

    @Override
    public boolean onCheckBoxPreferenceChange(Preference preference, Object newValue) {
        mView.setCheckBoxPreferenceSummary(preference, newValue);
        return true;
    }
}
