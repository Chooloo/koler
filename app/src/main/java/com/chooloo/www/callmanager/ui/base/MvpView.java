package com.chooloo.www.callmanager.ui.base;

import androidx.annotation.StringRes;

import com.chooloo.www.callmanager.util.ThemeUtils;

public interface MvpView {

    String[] onGetPermissions();

    void onSetup();

    boolean hasPermissions();

    boolean hasPermission(String permission);

    void askForPermissions();

    void showMessage(String message);

    void showMessage(@StringRes int stringResId);

    void showError(String message);

    void showError(@StringRes int stringResId);
}
