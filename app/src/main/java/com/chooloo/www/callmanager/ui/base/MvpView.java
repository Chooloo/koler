package com.chooloo.www.callmanager.ui.base;

import androidx.annotation.StringRes;

public interface MvpView {

    String[] onGetPermissions();

    void onSetup();

    boolean hasPermissions();

    boolean hasPermission(String permission);

    void askForPermission(String permission, int requestCode);

    void askForPermissions(String[] permissions, int requestCode);

    void askForPermissions();

    void showMessage(String message);

    void showMessage(@StringRes int stringResId);

    void showError(String message);

    void showError(@StringRes int stringResId);
}
