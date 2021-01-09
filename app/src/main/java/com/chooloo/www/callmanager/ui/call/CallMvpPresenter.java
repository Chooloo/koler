package com.chooloo.www.callmanager.ui.call;

import android.view.KeyEvent;

import com.chooloo.www.callmanager.ui.base.MvpPresenter;

public interface CallMvpPresenter<V extends CallMvpView> extends MvpPresenter<V> {

    void onAnswer();

    void onHangup();

    void onDialpadKeyClick(int keyCode, KeyEvent event);
}
