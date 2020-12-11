package com.chooloo.www.callmanager.ui.base;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;

public class BasePresenter<V extends MvpView> implements MvpPresenter<V>, LifecycleObserver {

    private static final String TAG = "BasePresenter";

    protected V mMvpView;

    @Override
    public void onAttach(V mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void onDetach() {
        mMvpView = null;
    }
}
