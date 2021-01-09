package com.chooloo.www.callmanager.ui.call;

import android.view.KeyEvent;

import com.chooloo.www.callmanager.ui.base.BasePresenter;

public class CallPresenter<V extends CallMvpView> extends BasePresenter<V> implements CallMvpPresenter<V> {
    @Override
    public boolean onBackPressed() {
        if (mMvpView.isDialpadOpened()) {
            mMvpView.toggleDialpad(false);
            return true;
        }
        return false;
    }

    @Override
    public void onAnswerClick() {
        mMvpView.switchToActiveCallUI();
        mMvpView.answer();
    }

    @Override
    public void onRejectClick() {
        mMvpView.reject();
    }

    @Override
    public void onAddCallClick() {
        // TODO this should get a call as a parameter or something idk do it
        mMvpView.addCall();
    }

    @Override
    public void onKeypadClick() {
        mMvpView.toggleDialpad(!mMvpView.isDialpadOpened());
    }

    @Override
    public void onMuteClick(boolean isActivated) {
        mMvpView.toggleHold(isActivated);
    }

    @Override
    public void onSpeakerClick(boolean isActivated) {
        mMvpView.toggleSpeaker(isActivated);
    }

    @Override
    public void onHoldClick(boolean isActivated) {
        mMvpView.toggleHold(isActivated);
    }

    @Override
    public void onDialpadKeyClick(int keyCode, KeyEvent event) {
        mMvpView.pressDialpadKey((char) event.getUnicodeChar());
    }

    @Override
    public void onDetailsChanged() {
        mMvpView.updateCall();
    }

    @Override
    public void onStateChanged() {
        mMvpView.updateState();
    }
}