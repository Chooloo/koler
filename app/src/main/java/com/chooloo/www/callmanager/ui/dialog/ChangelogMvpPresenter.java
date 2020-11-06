package com.chooloo.www.callmanager.ui.dialog;

import android.app.Dialog;

import com.chooloo.www.callmanager.ui.base.MvpPresenter;

public interface ChangelogMvpPresenter<V extends ChangelogMvpView> {
    Dialog onCreateDialog();
}
