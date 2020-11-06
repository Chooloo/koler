package com.chooloo.www.callmanager.ui.base;

import androidx.lifecycle.Lifecycle;

public interface MvpPresenter<V extends MvpView> {
    void onAttach(V mvpView, Lifecycle lifecycle);

    void onDetach();
}
