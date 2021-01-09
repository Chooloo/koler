package com.chooloo.www.callmanager.ui.call;

import android.view.KeyEvent;

import com.chooloo.www.callmanager.ui.base.MvpPresenter;

public interface CallMvpPresenter<V extends CallMvpView> extends MvpPresenter<V> {

    boolean onBackPressed();

    void onAnswerClick();

    void onRejectClick();

    void onAddCallClick();

    void onKeypadClick();

    void onMuteClick(boolean isActivated);

    void onSpeakerClick(boolean isActivated);

    void onHoldClick(boolean isActivated);

    void onDialpadKeyClick(int keyCode, KeyEvent event);

    void onDetailsChanged();

    void onStateChanged();
}
