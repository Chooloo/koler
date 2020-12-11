package com.chooloo.www.callmanager.ui.dialog;

import android.app.Dialog;

import androidx.lifecycle.Lifecycle;

import com.chooloo.www.callmanager.ui.base.BasePresenter;

public class ChangelogPresenter<V extends ChangelogMvpView> implements ChangelogMvpPresenter<V> {

    private V mMvpView;

    @Override
    public Dialog onCreateDialog() {
        return mMvpView.createDialog();
    }

    @Override
    public void onAttach(V mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void onDetach() {
        mMvpView = null;
    }
}
