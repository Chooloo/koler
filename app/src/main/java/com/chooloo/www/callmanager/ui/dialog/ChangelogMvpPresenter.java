package com.chooloo.www.callmanager.ui.dialog;

import android.app.Dialog;

import androidx.lifecycle.Lifecycle;

import com.chooloo.www.callmanager.ui.base.MvpPresenter;

public interface ChangelogMvpPresenter<V extends ChangelogMvpView> {

    Dialog onCreateDialog();

    void onAttach(V mvpView, Lifecycle lifecycle);

    void onDetach();
}
