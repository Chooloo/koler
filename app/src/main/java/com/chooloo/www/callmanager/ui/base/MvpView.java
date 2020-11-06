package com.chooloo.www.callmanager.ui.base;

import androidx.annotation.StringRes;

public interface MvpView {
    boolean hasPermissions();

    boolean hasPermission(String permission);

    void setRequiredPermissions(String[] permissions);

    void askForPermissions();

    void showMessage(String message);

    void showMessage(@StringRes int stringResId);

    void showError(String message);

    void showError(@StringRes int stringResId);
}
